package ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.tableInteractors;

import ru.nsu.ccfit.zhigalov.databases.exchanges.project.data.DatabaseController;
import ru.nsu.ccfit.zhigalov.databases.exchanges.project.domain.entities.PublicPhone;

public class PublicPhoneTableInteractor extends AbstractTableInteractor<PublicPhone> {
    public PublicPhoneTableInteractor(DatabaseController databaseController) {
        super(databaseController);
    }

    @Override
    protected String getUpdateQuery(PublicPhone phone) {
        return String.format(
                "update publicPhones set " +
                        "address_id = " +
                        "(select address_id from addresses where street = '%s' and house = '%s'), " +
                        "number_id = " +
                        "(select number_id from phoneNumbers where phone_number = '%s' and exchange_id = %d) " +
                        "where phone_id = %d ",
                phone.getAddress().getStreet(), phone.getAddress().getHouse(),
                phone.getPhoneNumber().getNumber(), phone.getPhoneNumber().getExchange().getId(),
                phone.getId()
        );
    }

    @Override
    protected String getInsertQuery(PublicPhone publicPhone) {
        return String.format(
                "insert into publicPhones(address_id, number_id) " +
                        "select address_id, number_id " +
                        "from ADDRESSES cross join PHONENUMBERS " +
                        "where street = '%s' and house = '%s' " +
                        "and PHONE_NUMBER = '%s' and EXCHANGE_ID = %d",
                publicPhone.getAddress().getStreet(),
                publicPhone.getAddress().getHouse(),
                publicPhone.getPhoneNumber().getNumber(),
                publicPhone.getPhoneNumber().getExchange().getId());
    }

    @Override
    protected String getDeleteQuery(PublicPhone publicPhone) {
        return String.format(
                "delete from publicPhones where phone_id = %d",
                publicPhone.getId() );
    }
}
