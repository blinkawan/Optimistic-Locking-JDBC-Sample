package com.agungsetiawan.optimisticlocking.dao;

import com.agungsetiawan.optimisticlocking.entity.Buku;
import com.agungsetiawan.optimisticlocking.exception.LockingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Agung Setiawan
 */
public class BukuDao {
    
    private Connection connection;
    
    public void setConnection(Connection connection){
        this.connection=connection;
    }
    
    public void save(Buku buku) throws SQLException{
        PreparedStatement ps=connection.prepareStatement("INSERT INTO buku(judul,penulis,versi)"
                + "values(?,?,1)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, buku.getJudul());
        ps.setString(2, buku.getPenulis());
        
        int id=ps.executeUpdate();
        buku.setId(id);
    }
    
    public void update(Buku buku) throws SQLException, LockingException{
        PreparedStatement ps=connection.prepareStatement("UPDATE buku SET judul=?,penulis=?,versi=versi+1"
                + " WHERE id=? AND versi=?");
        ps.setString(1, buku.getJudul());
        ps.setString(2, buku.getPenulis());
        ps.setInt(3, buku.getId());
        ps.setInt(4, buku.getVersion());
        
        int result=ps.executeUpdate();
        
        if(result<1){
            throw new LockingException("Locking Exception Occured");
        }
    }
    
    public Buku findOne(int id) throws SQLException{
        PreparedStatement ps=connection.prepareStatement("SELECT * FROM buku WHERE id=?");
        ps.setInt(1, id);
        ResultSet rs=ps.executeQuery();
        
        Buku buku=new Buku();
        while(rs.next()){
            buku.setId(rs.getInt("id"));
            buku.setJudul(rs.getString("judul"));
            buku.setPenulis(rs.getString("penulis"));
            buku.setVersion(rs.getInt("versi"));
        }
        
        return buku;
    }
    
}
