package com.ljw.crawler.mapper;

import com.ljw.crawler.entity.Skus;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.dao.DataAccessException;

import java.util.List;

@Mapper
public interface JobMapper {

    List<Skus> getSkus() throws DataAccessException;
}
