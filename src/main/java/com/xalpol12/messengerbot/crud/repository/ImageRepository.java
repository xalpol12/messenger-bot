package com.xalpol12.messengerbot.crud.repository;

import com.xalpol12.messengerbot.crud.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, String> {

    Optional<Image> findImageByCustomUri(String customUri);

    @Modifying
    @Query("DELETE FROM images image WHERE image.id IN :imageIds")
    void deleteAllInImageIdList(List<String> imageIds);
}
