create sequence request_id_seq start with 1
/
create or replace trigger queue_bi
    before insert
    on queue
    for each row
declare
    address_found integer;
begin
    select count(*) into address_found
    from exchangeswithAddresses
        where address_id = :new.address_id and exchange_id = :new.exchange_id;
    if address_found = 0 then
        raise_application_error(-20071, 'Адресс не доступен для выбранной АТС');
    end if;
    select request_id_seq.nextval into :New.request_id from dual;
    select sysdate into :New.request_date from dual;
end;
/

CREATE OR REPLACE PROCEDURE assign_number_to_client(a_request_id in integer, phone_phone_number in varchar,
                                                    phone_exchange_id in integer, phone_type in varchar)
    IS
    a_apartment     varchar(16);
    a_address_id    integer;
    a_person_id     varchar(32);
    a_exchange_id   integer;
    phone_number_id integer;
BEGIN
    if not phone_type in ('SIMPLE', 'PARALLEL', 'PAIRED') then
        raise_application_error(-20001, 'Assigned phone type must be on of following: simple, parallel, paired');
    end if;
    select apartment, address_id, person_id, exchange_id
    into a_apartment, a_address_id, a_person_id, a_exchange_id
    from QUEUE
    where REQUEST_ID = a_request_id;
    if a_exchange_id != phone_exchange_id then
        raise_application_error(-20006, 'Phone number must be from the exchange that is specified in the request');
    end if;
    select number_id
    into phone_number_id
    from phonenumbers
    where EXCHANGE_ID = phone_exchange_id
      and phone_phone_number = PHONE_NUMBER;
    UPDATE phonenumbers p
    SET p.type = phone_type
    where number_id = phone_number_id;
    insert into subscribers(person_id, address_id, apartment, number_id)
    values (a_person_id, a_address_id, a_apartment, phone_number_id);
    delete from queue where request_id = a_request_id;
    CHECK_NUMBER_OF_CABLES(a_exchange_id, a_address_id);
END;
/