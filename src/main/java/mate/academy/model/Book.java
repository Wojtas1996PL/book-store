package mate.academy.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "books")
@Data
@SQLDelete(sql = "UPDATE books SET isDeleted = true WHERE id =?")
@Where(clause = "isDeleted = false")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    @Column(unique = true)
    private String isbn;
    @NotNull
    private BigDecimal price;
    private String description;
    @Column(name = "cover_image")
    private String coverImage;
    @Column(name = "is_deleted", nullable = false, columnDefinition = "BIT")
    private boolean isDeleted = false;
}
