package com.testautomation.mesaj.database.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ORA12IIB10.MESSAGE_LOG")
@Data
public class ExampleOracle implements Serializable {

    private static final long serialVersionUID = 6829508373480950949L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", unique = true)
    private int ID;

    @Column(name = "RQUID")
    private String RQUID;

    @Column(name = "SERVICE_NAME")
    private String SERVICE_NAME;

    @Column(name = "OPERATION_NAME")
    private String OPERATION_NAME;

    @Column(name = "CHANNEL")
    private String CHANNEL;

    @Column(name = "BANKID")
    private String BANKID;

    @Column(name = "HOST_CODE")
    private String HOST_CODE;

    @Column(name = "IFX_CODE")
    private String IFX_CODE;

    @Column(name = "MESSAGE_DATE")
    private String MESSAGE_DATE;

    @Column(name = "MESSAGE")
    private String MESSAGE;

    @Column(name = "REF1")
    private String REF1;

    @Column(name = "REF2")
    private String REF2;

    @Column(name = "REF3")
    private String REF3;

    @Column(name = "IP_ADDRESS")
    private String IP_ADDRESS;

    @Column(name = "TRANSAC_TIME")
    private String TRANSAC_TIME;

    @Column(name = "MESSAGE_WAY_ID")
    private String MESSAGE_WAY_ID;

    @Column(name = "DEVICE_NAME")
    private String DEVICE_NAME;

    @Column(name = "USER_TX")
    private String USER_TX;

    @Column(name = "PROVIDER")
    private String PROVIDER;

    @Column(name = "COD_STATE_TX")
    private String COD_STATE_TX;

    @Column(name = "DESC_HOST_CODE")
    private String DESC_HOST_CODE;

    @Column(name = "TRX_TYPE_INVOCATION")
    private String TRX_TYPE_INVOCATION;

    @Column(name = "NUM_RETRY")
    private String NUM_RETRY;
}
