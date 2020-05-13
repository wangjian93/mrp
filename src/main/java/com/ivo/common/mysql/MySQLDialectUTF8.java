package com.ivo.common.mysql;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * 重写数据库方言，设置默认字符集为utf8
 * 解决hibernate在mysql自动建表数据会中文乱码现象
 * @author wj
 * @version 1.0
 */
public class MySQLDialectUTF8 extends MySQL5Dialect {

    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
