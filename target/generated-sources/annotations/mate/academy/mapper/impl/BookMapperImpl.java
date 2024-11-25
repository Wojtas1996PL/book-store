package mate.academy.mapper.impl;

import javax.annotation.processing.Generated;
import mate.academy.dto.BookDto;
import mate.academy.dto.CreateBookRequestDto;
import mate.academy.mapper.BookMapper;
import mate.academy.model.Book;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-11-25T15:21:00+0100",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class BookMapperImpl implements BookMapper {

    @Override
    public BookDto toDto(Book book) {
        if ( book == null ) {
            return null;
        }

        BookDto bookDto = new BookDto();

        bookDto.setId( book.getId() );
        if ( book.getTitle() != null ) {
            bookDto.setTitle( book.getTitle() );
        }
        if ( book.getAuthor() != null ) {
            bookDto.setAuthor( book.getAuthor() );
        }
        if ( book.getIsbn() != null ) {
            bookDto.setIsbn( book.getIsbn() );
        }
        if ( book.getPrice() != null ) {
            bookDto.setPrice( book.getPrice() );
        }
        if ( book.getDescription() != null ) {
            bookDto.setDescription( book.getDescription() );
        }
        if ( book.getCoverImage() != null ) {
            bookDto.setCoverImage( book.getCoverImage() );
        }

        return bookDto;
    }

    @Override
    public Book toModel(CreateBookRequestDto createBookRequestDto) {
        if ( createBookRequestDto == null ) {
            return null;
        }

        Book book = new Book();

        book.setId( createBookRequestDto.getId() );
        if ( createBookRequestDto.getTitle() != null ) {
            book.setTitle( createBookRequestDto.getTitle() );
        }
        if ( createBookRequestDto.getAuthor() != null ) {
            book.setAuthor( createBookRequestDto.getAuthor() );
        }
        if ( createBookRequestDto.getIsbn() != null ) {
            book.setIsbn( createBookRequestDto.getIsbn() );
        }
        if ( createBookRequestDto.getPrice() != null ) {
            book.setPrice( createBookRequestDto.getPrice() );
        }
        if ( createBookRequestDto.getDescription() != null ) {
            book.setDescription( createBookRequestDto.getDescription() );
        }
        if ( createBookRequestDto.getCoverImage() != null ) {
            book.setCoverImage( createBookRequestDto.getCoverImage() );
        }

        return book;
    }
}
