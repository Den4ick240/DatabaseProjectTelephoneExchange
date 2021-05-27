CREATE SEQUENCE exchange_id_seq START WITH 1
/
CREATE OR REPLACE TRIGGER cityExchanges_bi
    BEFORE INSERT
    ON CityExchanges
    FOR EACH ROW

DECLARE
    new_exchange_id integer;
BEGIN
    SELECT exchange_id_seq.nextval INTO new_exchange_id FROM dual;
    INSERT INTO Exchanges(exchange_id) VALUES (new_exchange_id);
    SELECT new_exchange_id
    INTO :NEW.EXCHANGE_ID
    FROM dual;
END;
/
CREATE OR REPLACE TRIGGER departmentalExchanges_bi
    BEFORE INSERT
    ON DepartmentalExchanges
    FOR EACH ROW

DECLARE
    new_exchange_id integer;
BEGIN
    SELECT exchange_id_seq.nextval INTO new_exchange_id FROM dual;
    INSERT INTO Exchanges(exchange_id) VALUES (new_exchange_id);
    SELECT new_exchange_id
    INTO :NEW.EXCHANGE_ID
    FROM dual;
END;
/
CREATE OR REPLACE TRIGGER institutionalExchanges_bi
    BEFORE INSERT
    ON InstitutionalExchanges
    FOR EACH ROW

DECLARE
    new_exchange_id integer;
BEGIN
    SELECT exchange_id_seq.nextval INTO new_exchange_id FROM dual;
    INSERT INTO Exchanges(exchange_id) VALUES (new_exchange_id);
    SELECT new_exchange_id
    INTO :NEW.EXCHANGE_ID
    FROM dual;
END;
/
CREATE OR REPLACE TRIGGER cityExchanges_ad
    AFTER DELETE
    ON CityExchanges
    FOR EACH ROW
BEGIN
    DELETE
    FROM Exchanges
    WHERE exchange_id = :OLD.exchange_id;
END;