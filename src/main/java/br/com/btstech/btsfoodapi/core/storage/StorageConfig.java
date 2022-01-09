package br.com.btstech.btsfoodapi.core.storage;

import br.com.btstech.btsfoodapi.domain.service.FotoStorageService;
import br.com.btstech.btsfoodapi.infrastructure.service.storage.LocalFotoStorageService;
import br.com.btstech.btsfoodapi.infrastructure.service.storage.S3FotosStorageService;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@AllArgsConstructor
@Configuration
public class StorageConfig {

    private StorageProperties storageProperties;

    @Bean
    public AmazonS3 amazonS3() {
        var credentials = new BasicAWSCredentials(storageProperties.getS3().getIdChaveAcesso(),
                storageProperties.getS3().getChaveAcessoSecreta());

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(storageProperties.getS3().getRegiao())
                .build();
    }

    @Bean
    public FotoStorageService fotoStorageService() {
        if (StorageProperties.TipoStorage.S3.equals(storageProperties.getTipo())) {
            return new S3FotosStorageService();
        } else {
            return new LocalFotoStorageService();
        }
    }
}
