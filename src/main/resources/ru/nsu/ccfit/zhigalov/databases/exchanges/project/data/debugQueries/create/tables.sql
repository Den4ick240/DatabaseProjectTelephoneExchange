CREATE TABLE Addresses
(
    address_id integer PRIMARY KEY,
    street     varchar(45) NOT NULL,
    house      varchar(8)  NOT NULL,

    UNIQUE (street, house)
)/

CREATE TABLE Cities
(
    name varchar(128) PRIMARY KEY
)/

CREATE TABLE Exchanges
(
    exchange_id integer PRIMARY KEY
)/

CREATE TABLE CityExchanges
(
    exchange_id integer PRIMARY KEY,
    name        varchar(128),

    FOREIGN KEY (exchange_id) references Exchanges (exchange_id),
    UNIQUE (name)
)/

CREATE TABLE InstitutionalExchanges
(
    exchange_id      integer NOT NULL,
    institution_name varchar(128),

    PRIMARY KEY (exchange_id),
    FOREIGN KEY (exchange_id) references Exchanges (exchange_id),
    UNIQUE (institution_name)
)/

CREATE TABLE DepartmentalExchanges
(
    exchange_id     integer NOT NULL,
    department_name varchar(128),

    PRIMARY KEY (exchange_id),
    FOREIGN KEY (exchange_id) references Exchanges (exchange_id),
    UNIQUE (department_name)
)/

CREATE TABLE ExchangesWithAddresses
(
    exchange_id      integer NOT NULL,
    address_id       integer NOT NULL,
    number_of_cables integer NOT NULL,

    PRIMARY KEY (exchange_id, address_id),
    FOREIGN KEY (exchange_id) references Exchanges (exchange_id),
    FOREIGN KEY (address_id) references Addresses (address_id)
)/

CREATE TABLE Genders
(
    gender varchar(45) PRIMARY KEY
)/

CREATE TABLE People
(
    person_id      varchar(32) PRIMARY KEY,
    name           varchar(44) NOT NULL,
    surname        varchar(44) NOT NULL,
    age            number(3)   NOT NULL,
    gender         varchar(44) NOT NULL,
    is_beneficiary number(1) CHECK (is_beneficiary IN (0, 1)),

    FOREIGN KEY (gender) REFERENCES Genders (gender)
)/

CREATE TABLE PhoneType
(
    type varchar(16) NOT NULL,

    PRIMARY KEY (type)
)/

CREATE TABLE PhoneNumbers
(
    number_id    integer PRIMARY KEY,
    phone_number varchar(12) NOT NULL,
    exchange_id  integer     NOT NULL,
    type         varchar(45) NOT NULL,

    UNIQUE (phone_number, exchange_id),
    FOREIGN KEY (type) REFERENCES PhoneType (type),
    FOREIGN KEY (exchange_id) REFERENCES Exchanges (exchange_id)
)/

CREATE TABLE PublicPhones
(
    phone_id   integer PRIMARY KEY,
    number_id  integer NOT NULL,
    address_id integer NOT NULL,

    FOREIGN KEY (number_id) REFERENCES PhoneNumbers (number_id),
    FOREIGN KEY (address_id) REFERENCES Addresses (address_id)
)/

CREATE TABLE LongDistanceCalls
(
    calling_number integer     NOT NULL,
    receiving_city varchar(45) NOT NULL,
    call_date      date        NOT NULL,
    length_minutes integer     NOT NULL,

    PRIMARY KEY (calling_number, receiving_city, call_date),
    FOREIGN KEY (calling_number) REFERENCES PhoneNumbers (number_id),
    FOREIGN KEY (receiving_city) REFERENCES Cities (name)
)/

CREATE TABLE LocalCalls
(
    call_date        date    NOT NULL,
    calling_number   integer NOT NULL,
    receiving_number integer NOT NULL,

    PRIMARY KEY (calling_number, call_date, receiving_number),
    FOREIGN KEY (calling_number) REFERENCES PhoneNumbers (number_id),
    FOREIGN KEY (receiving_number) REFERENCES PhoneNumbers (number_id)
)/

CREATE TABLE Subscribers
(
    subscriber_id   integer      NOT NULL,
    number_id       integer      NOT NULL,
    address_id      integer      NOT NULL,
    apartment       varchar(8)   NOT NULL,
    person_id       varchar(32)  NOT NULL,
    balance         number(10, 2) NOT NULL,
    state           varchar(8)   NOT NULL CHECK ( state in ('payed', 'expired', 'disabled') ),
    ld_state        varchar(8)   NOT NULL CHECK ( ld_state in ('payed', 'expired', 'disabled') ),
    expired_date    date,
    ld_expired_date date,

    PRIMARY KEY (subscriber_id),
    FOREIGN KEY (person_id) REFERENCES People (person_id),
    FOREIGN KEY (number_id) REFERENCES PhoneNumbers (number_id),
    FOREIGN KEY (address_id) REFERENCES Addresses (address_id)
)/

CREATE TABLE Queue
(
    request_id   integer PRIMARY KEY,
    person_id    varchar(32) NOT NULL,
    address_id   integer     NOT NULL,
    apartment    varchar(8)  NOT NULL,
    exchange_id  integer     NOT NULL,
    request_date date,

    UNIQUE (address_id, apartment, exchange_id),
    FOREIGN KEY (person_id) REFERENCES People (person_id),
    FOREIGN KEY (address_id) REFERENCES Addresses (address_id),
    FOREIGN KEY (exchange_id) REFERENCES Exchanges (exchange_id)
)/

CREATE TABLE Prices
(
    price_name  varchar(32) PRIMARY KEY,
    price_value number(6, 2) NOT NULL
)/