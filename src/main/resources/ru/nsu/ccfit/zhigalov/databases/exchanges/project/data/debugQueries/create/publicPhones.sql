CREATE SEQUENCE public_phone_id_seq START WITH 1/

CREATE OR REPLACE TRIGGER phoneNumber_bu
    BEFORE UPDATE
    ON PhoneNumbers
    FOR EACH ROW

BEGIN
    if :old.type != 'UNOCCUPIED' and :new.type in ('PUBLIC', 'SIMPLE') then
        raise_application_error(-20002, 'Phone number already occupied');
    end if;
    if :new.type in ('PAIRED', 'PARALLEL') and :old.type != 'SIMPLE' then
        raise_application_error(-20003, 'Only simple phones can be made paired or parallel');
    end if;
END;
/
CREATE OR REPLACE TRIGGER publicPhone_bi
    BEFORE INSERT
    ON PublicPhones
    FOR EACH ROW

DECLARE
    current_number_id integer;
    only_city_exchanges exception;
    address_found integer;
BEGIN
    SELECT :NEW.NUMBER_ID into current_number_id from DUAL;
    select count(*) into address_found
    from PHONENUMBERS pn inner join EXCHANGESWITHADDRESSES ewa on pn.exchange_id = ewa.EXCHANGE_ID
    where number_id = current_number_id and address_id = :NEW.address_id;
    if address_found = 0 then
        raise_application_error(-20008, 'public phone should be located at address avaliable for its exchange');
    end if;
    update PHONENUMBERS
    set TYPE = 'PUBLIC'
    where NUMBER_ID = current_number_id;
    SELECT public_phone_id_seq.nextval INTO :NEW.phone_id from DUAL;
END;
/
CREATE OR REPLACE TRIGGER publicPhone_bd
    BEFORE DELETE
    ON PublicPhones
    FOR EACH ROW

BEGIN
    UPDATE PhoneNumbers
        SET TYPE = 'UNOCCUPIED'
    WHERE NUMBER_ID = :OLD.NUMBER_ID;
END;
/