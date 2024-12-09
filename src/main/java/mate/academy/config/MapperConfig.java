package mate.academy.config;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@org.mapstruct.MapperConfig(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
        // Will NullValuePropertyMappingStrategy.IGNORE prevent NPE in classes?
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        // How should I name implementation package? I tried mate.academy.mapper but it didn't work.
        implementationPackage = "<PACKAGE_NAME>.impl"
)
public class MapperConfig {

}
