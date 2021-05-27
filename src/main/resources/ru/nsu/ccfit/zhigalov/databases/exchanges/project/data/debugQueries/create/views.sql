create view phone_numbers_view as
select number_id,
       phone_number,
       type,
       P.exchange_id as exchange_id,
       name,
       department_name,
       institution_name
from PHONENUMBERS P
         left join CITYEXCHANGES C2 on P.EXCHANGE_ID = C2.EXCHANGE_ID
         left join DEPARTMENTALEXCHANGES D on C2.EXCHANGE_ID = D.EXCHANGE_ID
         left join INSTITUTIONALEXCHANGES I2 on C2.EXCHANGE_ID = I2.EXCHANGE_ID
/

create view public_phones_view as
select phone_id,
       phone_number,
       street,
       house,
       P.exchange_id as exchange_id,
       name,
       department_name,
       institution_name
from PUBLICPHONES
         inner join PHONENUMBERS P on P.NUMBER_ID = PUBLICPHONES.NUMBER_ID
         inner join ADDRESSES A2 on A2.ADDRESS_ID = PUBLICPHONES.ADDRESS_ID
         inner join EXCHANGES E on P.EXCHANGE_ID = E.EXCHANGE_ID
         left join CITYEXCHANGES C2 on E.EXCHANGE_ID = C2.EXCHANGE_ID
         left join DEPARTMENTALEXCHANGES D on E.EXCHANGE_ID = D.EXCHANGE_ID
         left outer join INSTITUTIONALEXCHANGES I on E.EXCHANGE_ID = I.EXCHANGE_ID
/

create view exchange_view as
select EXCHANGES.exchange_id, name, department_name, institution_name
from EXCHANGES
         left join CITYEXCHANGES C2 on EXCHANGES.EXCHANGE_ID = C2.EXCHANGE_ID
         left join DEPARTMENTALEXCHANGES D on EXCHANGES.EXCHANGE_ID = D.EXCHANGE_ID
         left join INSTITUTIONALEXCHANGES I2 on EXCHANGES.EXCHANGE_ID = I2.EXCHANGE_ID

/


create view subscribers_view as
select subscriber_id,
       subscribers.number_id  as number_id,
       phone_number,
       type,
       exchange_id,
       pnv.name               as name,
       department_name,
       institution_name,
       subscribers.address_id as address_id,
       street,
       house,
       apartment,
       p.person_id,
       p.name                 as person_name,
       surname,
       age,
       gender,
       is_beneficiary,
       balance,
       state,
       ld_state,
       expired_date,
       ld_expired_date
from subscribers
         inner join ADDRESSES A2 on A2.ADDRESS_ID = SUBSCRIBERS.ADDRESS_ID
         inner join PEOPLE P on P.PERSON_ID = SUBSCRIBERS.PERSON_ID
         inner join PHONE_NUMBERS_VIEW PNV on subscribers.number_id = PNV.number_id
/
create view exchanges_with_addresses_view as
select exchange_view.exchange_id            as exchange_id,
       name,
       institution_name,
       department_name,
       ewa.address_id                       as address_id,
       street,
       house,
       number_of_cables,
       (number_of_cables - occupied_cables) as free_cables
from exchange_view
         inner join ExchangesWithAddresses ewa on exchange_view.exchange_id = ewa.exchange_id
         inner join Addresses adr on adr.address_id = ewa.ADDRESS_ID
         inner join (
    select ewa1.exchange_id, ewa1.address_id, count(phone_number) as occupied_cables
    from exchangeswithaddresses ewa1
             left join subscribers_view s
                       on ewa1.exchange_id = s.exchange_id and ewa1.ADDRESS_ID = s.ADDRESS_ID
    group by(ewa1.exchange_id, ewa1.address_id)
) fc on fc.exchange_id = ewa.exchange_id and fc.address_id = ewa.address_id
/

create view queue_view as
select request_id,
       street,
       house,
       apartment,
       ewav.exchange_id as exchange_id,
       ewav.name        as name,
       department_name,
       institution_name,
       p.PERSON_ID      as person_id,
       p.name           as person_name,
       surname,
       gender,
       age,
       is_beneficiary,
       request_date,
       number_of_cables,
       free_cables
from queue
         inner join PEOPLE P on P.PERSON_ID = QUEUE.PERSON_ID
         inner join EXCHANGES_WITH_ADDRESSES_VIEW EWAV
                    on queue.address_id = EWAV.address_id and queue.exchange_id = ewav.exchange_id
/
create view cities_with_number_of_calls as
select name, count(CALLING_NUMBER) as number_of_calls
from (select name, CALLING_NUMBER
      from cities
               left join LONGDISTANCECALLS L on L.RECEIVING_CITY = cities.name
     )
group by (name)
/