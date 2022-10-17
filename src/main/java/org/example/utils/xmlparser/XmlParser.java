package org.example.utils.xmlparser;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.model.BankOperation;
import org.example.model.Currency;
import org.example.model.PaymentStatus;
import org.example.utils.FileUtils;
import org.example.utils.Parser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class XmlParser implements Parser {

    static final String TAG_OPERATION_MAME = "bankOperation";
    static final String TAG_ID = "id";
    static final String TAG_SEND_CARD_NUMBER = "sendCardNumber";
    static final String TAG_GET_CARD_NUMBER = "getCardNumber";
    static final String TAG_SEND_SUM = "sendSum";
    static final String TAG_CURRENCY = "currency";
    static final String TAG_SEND_DATE_TIME = "sendDateTime";
    static final String TAG_UPDATE_TIME = "updateTime";
    static final String TAG_PAYMENT_PURPOSE = "paymentPurpose";
    static final String TAG_PAYMENT_STATUS = "paymentStatus";
    static final String XML_FILE = "src/main/resources/bankOperation.xml";

    @Override
    public void toFile(List<BankOperation> bankOperations) {
        FileUtils.clearFile(XML_FILE);
        try {
            XMLOutputFactory output = XMLOutputFactory.newInstance();
            XMLStreamWriter writer = output.createXMLStreamWriter(new FileWriter(XML_FILE));
            writer.writeStartDocument("1.0");
            writer.writeStartElement("BankOperations");
            for (BankOperation bankOperation : bankOperations) {
                writer.writeStartElement(TAG_OPERATION_MAME);

                writer.writeStartElement(TAG_ID);
                writer.writeCharacters(String.valueOf(bankOperation.getId()));
                writer.writeEndElement();

                writer.writeStartElement(TAG_SEND_CARD_NUMBER);
                writer.writeCharacters(bankOperation.getSendCardNumber());
                writer.writeEndElement();

                writer.writeStartElement(TAG_GET_CARD_NUMBER);
                writer.writeCharacters(bankOperation.getGetCardNumber());
                writer.writeEndElement();

                writer.writeStartElement(TAG_SEND_SUM);
                writer.writeCharacters(String.valueOf(bankOperation.getSendSum()));
                writer.writeEndElement();

                writer.writeStartElement(TAG_CURRENCY);
                writer.writeCharacters(String.valueOf(bankOperation.getCurrency()));
                writer.writeEndElement();

                writer.writeStartElement(TAG_SEND_DATE_TIME);
                writer.writeCharacters(String.valueOf(bankOperation.getSendDateTime()));
                writer.writeEndElement();

                writer.writeStartElement(TAG_UPDATE_TIME);
                writer.writeCharacters(String.valueOf(bankOperation.getUpdateTime()));
                writer.writeEndElement();

                writer.writeStartElement(TAG_PAYMENT_PURPOSE);
                writer.writeCharacters(String.valueOf(bankOperation.getPaymentPurpose()));
                writer.writeEndElement();

                writer.writeStartElement(TAG_PAYMENT_STATUS);
                writer.writeCharacters(String.valueOf(bankOperation.getPaymentStatus()));
                writer.writeEndElement();
                writer.writeEndElement();
            }
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
        } catch (XMLStreamException | IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public List<BankOperation> toObjectList() {
        List<BankOperation> bankOperations = new ArrayList<>();
        try {
            DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = documentBuilder.parse(XML_FILE);
            Node root = document.getDocumentElement();
            NodeList transactions = root.getChildNodes();
            for (int i = 0; i < transactions.getLength(); i++) {
                Node transaction = transactions.item(i);
                if (transaction.getNodeType() != Node.TEXT_NODE) {
                    NodeList bookProps = transaction.getChildNodes();
                    BankOperation bankOperation = new BankOperation();
                    for (int j = 0; j < bookProps.getLength(); j++) {
                        Node bankProp = bookProps.item(j);
                        if (bankProp.getNodeType() != Node.TEXT_NODE) {
                            String tagName = bankProp.getNodeName();
                            //           System.out.println(tagName);
                            switch (tagName) {
                                case TAG_ID ->
                                        bankOperation.setId(Integer.parseInt(bankProp.getChildNodes().item(0).getTextContent()));
                                case TAG_GET_CARD_NUMBER ->
                                        bankOperation.setGetCardNumber((bankProp.getChildNodes().item(0).getTextContent()));
                                case TAG_SEND_CARD_NUMBER ->
                                        bankOperation.setSendCardNumber(((bankProp.getChildNodes().item(0).getTextContent())));
                                case TAG_SEND_SUM ->
                                        bankOperation.setSendSum(Float.valueOf((((bankProp.getChildNodes().item(0).getTextContent())))));
                                case TAG_CURRENCY ->
                                        bankOperation.setCurrency(Currency.valueOf(((bankProp.getChildNodes().item(0).getTextContent()))));
                                case TAG_SEND_DATE_TIME ->
                                        bankOperation.setSendDateTime(LocalDateTime.parse(((bankProp.getChildNodes().item(0).getTextContent()))));
                                case TAG_UPDATE_TIME ->
                                        bankOperation.setUpdateTime(LocalDateTime.parse(((bankProp.getChildNodes().item(0).getTextContent()))));
                                case TAG_PAYMENT_PURPOSE ->
                                        bankOperation.setPaymentPurpose(((bankProp.getChildNodes().item(0).getTextContent())));
                                case TAG_PAYMENT_STATUS ->
                                        bankOperation.setPaymentStatus(PaymentStatus.valueOf(((bankProp.getChildNodes().item(0).getTextContent()))));
                            }
                        }
                    }
                    bankOperations.add(bankOperation);
                }
            }
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            ex.printStackTrace(System.out);
        }
        return bankOperations;
    }
}