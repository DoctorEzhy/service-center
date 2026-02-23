package dao;

import model.Part;  // <-- вот этот импорт
import java.util.List;

public interface PartDAO {

    void save(Part part);
    Part findById(Long id);
    List<Part> findAll();
    void update(Part part);
    void delete(Long id);

    List<Part> searchByName(String name);
}
