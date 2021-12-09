package gg.dstore.domain.dto.file.response;

import gg.dstore.domain.entity.BannerEntity;
import gg.dstore.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BannerResponse extends Response {
    List<BannerEntity> bannerLocation;
}
