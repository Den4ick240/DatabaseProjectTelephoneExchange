create role zh186_subscriber_role
/
grant connect to zh186_subscriber_role
/
grant execute on TOP_UP_BALANCE to zh186_subscriber_role
/
grant select on "18206_ZHIGALOV".PEOPLE to zh186_subscriber_role
/
grant select on "18206_ZHIGALOV".ADDRESSES to zh186_subscriber_role
/
grant select on "18206_ZHIGALOV".SUBSCRIBERS_VIEW to zh186_subscriber_role
/
grant select on "18206_ZHIGALOV".EXCHANGE_VIEW to zh186_subscriber_role
/
grant select on "18206_ZHIGALOV".EXCHANGES_WITH_ADDRESSES_VIEW to zh186_subscriber_role
/
grant insert on "18206_ZHIGALOV".QUEUE to zh186_subscriber_role
/
grant select on "18206_ZHIGALOV".PHONE_NUMBERS_VIEW to zh186_subscriber_role
/
grant select on "18206_ZHIGALOV".CITIES to zh186_subscriber_role
/
grant execute on "18206_ZHIGALOV".LONG_DISTANCE_CALL to zh186_subscriber_role
/
grant execute on "18206_ZHIGALOV".LOCAL_CALL to zh186_subscriber_role
/
grant execute on "18206_ZHIGALOV".GET_LOCAL_CALLS_ENABLE_PRICE to zh186_subscriber_role
/
grant execute on "18206_ZHIGALOV".GET_LD_CALLS_ENABLE_PRICE to zh186_subscriber_role
/
grant execute on "18206_ZHIGALOV".ENABLE_LOCAL_CALLS to zh186_subscriber_role
/
grant execute on "18206_ZHIGALOV".ENABLE_LD_CALLS to zh186_subscriber_role
/
grant execute on "18206_ZHIGALOV".TOP_UP_BALANCE to zh186_subscriber_role
/
