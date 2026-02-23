package dao.impl;

import dao.PartDAO;
import model.Part;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class PartDAOImpl implements PartDAO {

    private static final Logger logger = LoggerFactory.getLogger(PartDAOImpl.class);

    @Override
    public void save(Part part) {
        String sql = "INSERT INTO parts(name, quantity, price) VALUES (?, ?, ?)";

        try (Connection conn = util.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, part.getName());
            ps.setInt(2, part.getQuantity());
            ps.setDouble(3, part.getPrice());

            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Ошибка сохранения запчасти", e);
        }
    }

    @Override
    public List<Part> searchByName(String name) {
        String sql = "SELECT * FROM parts WHERE name LIKE ?";
        List<Part> parts = new ArrayList<>();

        try (Connection conn = util.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, "%" + name + "%");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                parts.add(new Part(
                        rs.getLong("id"),
                        rs.getString("name"),
                        rs.getInt("quantity"),
                        rs.getDouble("price")
                ));
            }

        } catch (SQLException e) {
            logger.error("Ошибка поиска запчастей", e);
        }

        return parts;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM parts WHERE id = ?";

        try (Connection conn = util.DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            logger.error("Ошибка удаления запчасти", e);
        }
    }

    // Остальные методы интерфейса (findById, findAll, update, delete) тоже реализуй аналогично
}
