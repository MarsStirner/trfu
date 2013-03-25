/**
 * SMSWebServiceSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package ru.smsdelivery;

public interface SMSWebServiceSoap extends java.rmi.Remote {

    /**
     * Получить баланс пользователя (количество начисленных СМС)<br/>Параметры:<br/>&nbsp;&nbsp;<b>userName</b>
     * - логин пользователя, полученный при регистрации<br/>&nbsp;&nbsp;<b>password</b>
     * - пароль пользователя, полученный при регистрации<br/>Возвращаемые
     * значения - результат получения баланса пользователя и баланс пользователя
     * (количество начисленных СМС)
     */
    public ru.smsdelivery.GetUserBalanceResponse getBalance(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Получить статус отправленного СМС<br/>Параметры:<br/>&nbsp;&nbsp;<b>userName</b>
     * - логин пользователя, полученный при регистрации<br/>&nbsp;&nbsp;<b>password</b>
     * - пароль пользователя, полученный при регистрации<br/>&nbsp;&nbsp;<b>messageID</b>
     * - идентификатор отправленного СМС (возвращается SendMessage)<br/>Возвращаемые
     * значения - результат получения статуса отправленного СМС и статус
     * отправленного СМС
     */
    public ru.smsdelivery.GetStatusResponse getMessageStatus(java.lang.String userName, java.lang.String password, int messageID) throws java.rmi.RemoteException;

    /**
     * Получить статус отправленного СМС и время доставки СМС<br/>Параметры:<br/>&nbsp;&nbsp;<b>userName</b>
     * - логин пользователя, полученный при регистрации<br/>&nbsp;&nbsp;<b>password</b>
     * - пароль пользователя, полученный при регистрации<br/>&nbsp;&nbsp;<b>messageID</b>
     * - идентификатор отправленного СМС (возвращается SendMessage)<br/>Возвращаемые
     * значения - результат получения статуса отправленного СМС, статус отправленного
     * СМС и время доставки СМС (UTC, в виде 2011-12-31T23:59:59Z), если
     * СМС доставлено
     */
    public ru.smsdelivery.GetStatus2Response getMessageStatus2(java.lang.String userName, java.lang.String password, int messageID) throws java.rmi.RemoteException;

    /**
     * Отправить СМС<br/>Параметры:<br/>&nbsp;&nbsp;<b>userName</b>
     * - логин пользователя, полученный при регистрации<br/>&nbsp;&nbsp;<b>password</b>
     * - пароль пользователя, полученный при регистрации<br/>&nbsp;&nbsp;<b>isFlash</b>
     * - признак флэш-СМС (СМС с этим признаком не сохраняется в памяти телефона
     * и может содержать не более 70 кириллических или 160 латинских символов)<br/>&nbsp;&nbsp;&nbsp;&nbsp;<i>из-за
     * особенностей работы некоторых операторов параметр не поддерживается,
     * его значение должно быть установлено в false</i><br/>&nbsp;&nbsp;<b>lifeTime</b>
     * - максимальное время жизни СМС в минутах (не дольше, чем в течение
     * этого времени будут происходить попытки доставить СМС, если не получилось
     * с первого раза)<br/>&nbsp;&nbsp;&nbsp;&nbsp;<i>из-за особенностей
     * работы некоторых операторов заданное значение параметра корректируется,
     * чтобы оно было не менее 24 часов</i><br/>&nbsp;&nbsp;<b>destNumber</b>
     * - номер телефона, на который отправляется СМС (в виде 79114003793)<br/>&nbsp;&nbsp;<b>senderAddr</b>
     * - цифробуквенный адрес отправителя СМС (изначально можно использовать
     * только smsdelivery)<br/>&nbsp;&nbsp;<b>text</b> - текст СМС<br/>Возвращаемые
     * значения - результат отправки СМС, идентификатор отправленного СМС
     * (можно использовать в GetMessageStatus) и количество частей в отправленном
     * СМС
     */
    public ru.smsdelivery.MessageResponse sendMessage(java.lang.String userName, java.lang.String password, boolean isFlash, int lifeTime, java.lang.String destNumber, java.lang.String senderAddr, java.lang.String text) throws java.rmi.RemoteException;

    /**
     * Получить список адресов пользователя
     */
    public ru.smsdelivery.GetSenderAddressesResp getSenderAddresses(java.lang.String userName, java.lang.String password) throws java.rmi.RemoteException;

    /**
     * Отправить адрес на модерацию
     */
    public ru.smsdelivery.AddSenderAddressWithModerationResp addSenderAddressWithModeration(java.lang.String userName, java.lang.String password, java.lang.String address) throws java.rmi.RemoteException;
}
