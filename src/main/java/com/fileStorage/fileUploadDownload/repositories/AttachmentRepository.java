package com.fileStorage.fileUploadDownload.repositories;

import com.fileStorage.fileUploadDownload.models.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Boolean existsByfileName(String fileName);
}
