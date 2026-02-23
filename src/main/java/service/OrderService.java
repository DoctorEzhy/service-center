package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.DBConnection;

public class OrderService {

    public void createOrder(int partId, int quantity) {

        String checkPartSql = "SELECT quantity FROM parts WHERE id = ?";
        String updatePartSql = "UPDATE parts SET quantity = quantity - ? WHERE id = ?";
        String insertOrderSql = "INSERT INTO orders(part_id, quantity, status) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            // Проверка существования детали и доступного количества
            try (PreparedStatement checkStmt = conn.prepareStatement(checkPartSql)) {
                checkStmt.setInt(1, partId);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new SQLException("Деталь с ID " + partId + " не существует.");
                    }
                    int availableQuantity = rs.getInt("quantity");
                    if (availableQuantity < quantity) {
                        throw new SQLException("Недостаточно деталей на складе. Доступно: " + availableQuantity);
                    }
                }
            }

            // Обновление количества детали
            try (PreparedStatement updateStmt = conn.prepareStatement(updatePartSql);
                 PreparedStatement insertStmt = conn.prepareStatement(insertOrderSql)) {

                updateStmt.setInt(1, quantity);
                updateStmt.setInt(2, partId);
                updateStmt.executeUpdate();

                insertStmt.setInt(1, partId);
                insertStmt.setInt(2, quantity);
                insertStmt.setString(3, "CREATED");
                insertStmt.executeUpdate();

                conn.commit();
                System.out.println("Заказ успешно создан!");
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            }

        } catch (SQLException e) {
            System.err.println("Ошибка при создании заказа: " + e.getMessage());
        }
    }
}