package gg.jominsubyungsin.domain.dto.file.response;

import gg.jominsubyungsin.domain.entity.BannerEntity;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BennerResponse extends Response {

	List<BannerEntity> bennerLocation;
}
