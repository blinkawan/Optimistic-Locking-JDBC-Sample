package com.agungsetiawan.optimisticlocking.dao;

import com.agungsetiawan.optimisticlocking.entity.Buku;
import com.agungsetiawan.optimisticlocking.exception.LockingException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**
 *
 * @author Agung Setiawan
 */
public class BukuDaoTest {
    
    private MysqlDataSource dataSource;
    private  BukuDao bukuDao;
    
    @Before
    public void setUp() throws SQLException {
        dataSource=new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("");
        dataSource.setDatabaseName("astral");
        dataSource.setServerName("localhost");
        
        bukuDao=new BukuDao();
        bukuDao.setConnection(dataSource.getConnection());
    }
    
    @After
    public void tearDown() throws SQLException{
        dataSource.getConnection().close();
    }

    @Test(expected = LockingException.class)
    public void testDaoShouldThrowLockingException() throws SQLException, LockingException {
        Buku bukuSatu=bukuDao.findOne(1);
        Buku bukuDua=bukuDao.findOne(1);
        
        bukuSatu.setPenulis("Pramoedya Ananta T");
        bukuDua.setPenulis("Pramoedya AT");
        
        bukuDao.update(bukuSatu); //sukses
        bukuDao.update(bukuDua); //gagal
    }
}