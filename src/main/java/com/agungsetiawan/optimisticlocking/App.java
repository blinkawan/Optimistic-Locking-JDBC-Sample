package com.agungsetiawan.optimisticlocking;

import com.agungsetiawan.optimisticlocking.dao.BukuDao;
import com.agungsetiawan.optimisticlocking.entity.Buku;
import com.agungsetiawan.optimisticlocking.exception.LockingException;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import java.sql.SQLException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws SQLException, LockingException
    {
        MysqlDataSource dataSource=new MysqlDataSource();
        dataSource.setUser("root");
        dataSource.setPassword("");
        dataSource.setDatabaseName("astral");
        dataSource.setServerName("localhost");
        
        BukuDao bukuDao=new BukuDao();
        bukuDao.setConnection(dataSource.getConnection());
        
//        Buku buku=new Buku();
//        buku.setJudul("Arus Balik");
//        buku.setPenulis("Pramoedya Ananta Toer");
        
        Buku bukuSatu=bukuDao.findOne(1);
        Buku bukuDua=bukuDao.findOne(1);
        
        bukuSatu.setPenulis("Pramoedya Ananta T");
        bukuDua.setPenulis("Pramoedya AT");
        
        bukuDao.update(bukuSatu); //sukses
        bukuDao.update(bukuDua); //gagal
        
        dataSource.getConnection().close();
        
    }
}
