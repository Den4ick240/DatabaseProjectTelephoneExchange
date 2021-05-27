create sequence subscriber_id_seq start with 1
/
create or replace function get_free_cables(i_exchange_id in integer, i_address_id in integer)
    return integer
    is
    i_number_of_cables integer;
    i_occupied_cables  integer;
begin
    select count(*)
    into i_occupied_cables
    from (
             select distinct phone_number
             from subscribers s
                      inner join phonenumbers pn on s.number_id = pn.number_id
             where pn.exchange_id = i_exchange_id
               and s.address_id = i_address_id
         );
    select number_of_cables
    into i_number_of_cables
    from exchangeswithaddresses
    where exchange_id = i_exchange_id
      and address_id = i_address_id;
    return i_number_of_cables - i_occupied_cables;
end;
/
create or replace procedure check_number_of_cables(i_exchange_id in integer, i_address_id in integer)
is
begin
    if get_free_cables(i_exchange_id, i_address_id) < 0 then
        raise_application_error(-20018, 'Ќарушено ограничение на количество кабелей');
    end if;
end;
/
create
    or
    replace trigger subscribers_bi
    before
        insert
    on subscribers
    for each row
declare
    phone_type      varchar(16);
    violation_count integer;
    address_found   integer;
begin
    select count(*)
    into address_found
    from phonenumbers pn
             inner join EXCHANGESWITHADDRESSES E on pn.EXCHANGE_ID = E.EXCHANGE_ID
    where pn.NUMBER_ID = :new.number_id
      and e.ADDRESS_ID = :new.address_id;
    if address_found = 0 then
        raise_application_error(-20008, 'public phone should be located at address available for its exchange');
    end if;
    select p.type
    into phone_type
    from phonenumbers p
    where p.number_id = :new.number_id;
    if not phone_type in ('SIMPLE', 'PARALLEL', 'PAIRED') then
        raise_application_error(-20001, 'Assigned phone type must be on of following: simple, parallel, paired');
    end if;
    select count(*)
    into violation_count
    from SUBSCRIBERS
    where number_id = :new.number_id
      and not address_id = :new.address_id;
    if violation_count > 0 then
        raise_application_error(-20005, 'Subscribers with the same phone number must be in the same house');
    end if;
    select subscriber_id_seq.nextval into :new.subscriber_id from dual;
    select 0 into :new.balance from dual;
    select 'disabled' into :new.state from dual;
    select 'disabled' into :new.ld_state from dual;
end;
/
create
    or
    replace procedure top_up_balance(current_subscriber_id in number, payment_value number)
    is
    current_balance number(10, 2);
    new_balance     number(10, 2);
    n_state         varchar(32);
    n_ld_state      varchar(32);
begin
    select balance, state, ld_state
    into current_balance, n_state, n_ld_state
    from subscribers
    where current_subscriber_id = subscribers.subscriber_id;
    new_balance := current_balance + payment_value;

    if new_balance >= 0 then
        if n_state = 'expired' then
            n_state := 'payed';
        end if;
        if n_ld_state = 'expired' then
            n_ld_state := 'payed';
        end if;
    end if;
    update subscribers
    set balance  = new_balance,
        state    = n_state,
        ld_state = n_ld_state
    where SUBSCRIBERS.SUBSCRIBER_ID = current_subscriber_id;
end;
/

create
    or
    replace procedure local_call(calling_subscriber_id in number, receiving_phone_number in varchar)
    is
    state                varchar(32);
    calling_number_id    integer;
    receiving_number_id  integer;
    receiving_phone_type varchar(32);
begin
    select subscribers.state, subscribers.number_id
    into state, calling_number_id
    from subscribers
    where subscribers.subscriber_id = calling_subscriber_id;

    if state = 'disabled' then
        raise_application_error(-20009, 'Local calls are disabled, call cannot be made');
    end if;

    select number_id, phoneNumbers.type
    into receiving_number_id, receiving_phone_type
    from phoneNumbers
    where phoneNumbers.phone_number = receiving_phone_number;

    if not receiving_phone_type in ('SIMPLE', 'PARALLEL', 'PAIRED') then
        raise_application_error(-20010, 'Local call can be made only to occupied phone');
    end if;

    insert into LOCALCALLS (CALL_DATE, CALLING_NUMBER, RECEIVING_NUMBER)
    values (sysdate, calling_number_id, receiving_number_id);
end;
/
create
    or
    replace procedure long_distance_call(calling_subscriber_id in integer, city in varchar,
                                         call_length_minutes integer)
    is
    current_ld_state  varchar(32);
    calling_number_id integer;
    n_price           number(10, 2);
    n_discount        number(10, 2);
    n_beneficiary     integer;
    n_balance         number(10, 2);
begin
    select ld_state, number_id, is_beneficiary, balance
    into current_ld_state, calling_number_id, n_beneficiary, n_balance
    from subscribers_view
    where subscribers_view.subscriber_id = calling_subscriber_id;

    if current_ld_state = 'disabled' then
        raise_application_error(-20011, 'Long distance calls disabled, call cannot be made');
    end if;

    select price_value into n_price from prices where price_name = 'ld_call_per_minute';
    if n_beneficiary = 1 then
        select price_value into n_discount from prices where price_name = 'beneficiary_discount_percent';
    else
        n_discount := 100.00;
    end if;
    n_balance := n_balance - call_length_minutes * n_price * n_discount * 0.01;
    if n_balance < 0 and current_ld_state = 'payed' then
        update subscribers
        set ld_state        = 'expired',
            ld_expired_date = sysdate
        where subscribers.subscriber_id = calling_subscriber_id;
    end if;
    update subscribers set balance = n_balance where subscriber_id = calling_subscriber_id;
    insert into longDistanceCalls(CALLING_NUMBER, RECEIVING_CITY, CALL_DATE, LENGTH_MINUTES)
    values (calling_number_id, city, sysdate, call_length_minutes);
end;
/
create
    or
    replace function get_local_calls_enable_price(n_subscriber_id in integer)
    return number is
    n_subscription_fee     number(6, 2);
    n_connection_fee       number(6, 2);
    n_beneficiary_discount number(6, 2) := 100.00;
    n_total_price          number(6, 2);
    n_is_beneficiary       integer;
begin
    select price_value into n_subscription_fee from prices where price_name = 'subscription_fee';
    select price_value into n_connection_fee from prices where price_name = 'connection_fee';
    select is_beneficiary into n_is_beneficiary from subscribers_view where subscriber_id = n_subscriber_id;
    if n_is_beneficiary = 1 then
        select price_value
        into n_beneficiary_discount
        from prices
        where price_name = 'beneficiary_discount_percent';
    end if;
    n_total_price := (n_subscription_fee + n_connection_fee) * n_beneficiary_discount * 0.01;
    return n_total_price;
end;
/
create
    or
    replace function get_ld_calls_enable_price(n_subscriber_id in integer)
    return number is
    n_connection_fee       number(6, 2);
    n_beneficiary_discount number(6, 2) := 100.00;
    n_total_price          number(6, 2);
    n_is_beneficiary       integer;
begin
    select price_value into n_connection_fee from prices where price_name = 'ld_connection_fee';
    select is_beneficiary into n_is_beneficiary from subscribers_view where subscriber_id = n_subscriber_id;
    if n_is_beneficiary = 1 then
        select price_value
        into n_beneficiary_discount
        from prices
        where price_name = 'beneficiary_discount_percent';
    end if;
    n_total_price := n_connection_fee * n_beneficiary_discount * 0.01;
    return n_total_price;
end;
/
create
    or
    replace procedure enable_local_calls(n_subscriber_id in integer)
    is
    v_balance_state varchar(32);
    n_balance       number(10, 2);
    n_price         number(10, 2);
begin
    select subscribers.state, balance
    into v_balance_state, n_balance
    from subscribers
    where subscribers.subscriber_id = n_subscriber_id;
    if not v_balance_state = 'disabled' then
        raise_application_error(-20011, 'local calls already enabled');
    end if;
    n_price := get_local_calls_enable_price(n_subscriber_id);
    if n_price > n_balance then
        raise_application_error(-20012, 'not enough money to enable local calls');
    end if;
    update subscribers
    set balance = n_balance - n_price,
        state   = 'payed'
    where subscribers.subscriber_id = n_subscriber_id;
end;
/
create
    or
    replace procedure enable_ld_calls(n_subscriber_id in integer)
    is
    v_ld_balance_state varchar(32);
    v_balance_state    varchar(32);
    n_balance          number(10, 2);
    n_price            number(10, 2);
begin
    select state, subscribers.ld_state, balance
    into v_balance_state, v_ld_balance_state, n_balance
    from subscribers
    where subscribers.subscriber_id = n_subscriber_id;
    if v_balance_state = 'disabled' then
        raise_application_error(-20015, 'Local calls must be enabled to enable long distance calls');
    end if;
    if not v_ld_balance_state = 'disabled' then
        raise_application_error(-20013, 'long distance calls already enabled');
    end if;
    n_price := get_ld_calls_enable_price(n_subscriber_id);
    if n_price > n_balance then
        raise_application_error(-20014, 'not enough money to enable long distance calls');
    end if;
    update subscribers
    set balance  = n_balance - n_price,
        ld_state = 'payed'
    where subscribers.subscriber_id = n_subscriber_id;
end;
/
create
    or
    replace procedure charge_subscription_fee
    is
    n_price            number(10, 2);
    n_discount         number(10, 2);
    n_discount_reverse number(10, 2);
begin
    select price_value into n_price from prices where price_name = 'subscription_fee';
    select price_value into n_discount from prices where price_name = 'beneficiary_discount_percent';
    n_discount_reverse := 1 - n_discount * 0.01;
    update subscribers_view
    set balance = (balance - n_price * (1 - is_beneficiary * n_discount_reverse))
    where state = 'payed';
    update subscribers
    set state        = 'expired',
        expired_date = sysdate
    where state = 'payed'
      and balance <= 0;
    update subscribers
    set ld_state        = 'expired',
        ld_expired_date = sysdate
    where balance <= 0
      and ld_state = 'payed';
end;
/
create
    or
    replace procedure disable_expired_clients
    is
begin
    update subscribers
    set state = 'disabled'
    where state = 'expired'
      and (sysdate - expired_date) > 0;
    update subscribers
    set ld_state = 'disabled'
    where ld_state = 'expired'
      and (sysdate - ld_expired_date) > 0;
end;
/