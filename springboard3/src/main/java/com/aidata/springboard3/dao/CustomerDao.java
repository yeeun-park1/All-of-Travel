package com.aidata.springboard3.dao;

import com.aidata.springboard3.dto.CustomerDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CustomerDao {
    int selectcid(String cid);

    void insertCustomer(CustomerDto customer);

    String selectPwd(String cid);

    CustomerDto selectCustomer(String cid);

    void deleteMember(String cid);
}
