package impl;

public class PartDAOImpl implements PartDAO {

    private static final Logger logger = LoggerFactory.getLogger(PartDAOImpl.class);

    @Override
    public void save(Part part) {
        String sql = "INSERT INTO parts(name, quantity, price) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
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

        try (Connection conn = DBConnection.getConnection();
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
}
