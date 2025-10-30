package com.cesarschool.catalisabackend.models.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    public Optional<Product> findByNameIgnoreCase(String name);

    public Optional<Product> deleteProductByName(String name);

    public boolean existsByNameIgnoreCase(String name);

    List<Product> findAllByTypeOrderByNameAsc(ProductType type);

    @Query("""
   select p from Product p
   where (:q is null or :q = '' 
      or lower(p.name) like lower(concat('%', :q, '%'))
      or lower(p.type) like lower(concat('%', :q, '%')))
""")
    List<Product> search(@Param("q") String q);

}
