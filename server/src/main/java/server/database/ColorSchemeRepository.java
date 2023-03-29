package server.database;

import commons.ColorScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColorSchemeRepository extends JpaRepository<ColorScheme,Long> {
}
