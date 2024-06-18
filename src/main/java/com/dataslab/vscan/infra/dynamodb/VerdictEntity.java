package com.dataslab.vscan.infra.dynamodb;

import com.dataslab.vscan.service.domain.Verdict;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.Optional;

@DynamoDbBean
@Data
@NoArgsConstructor
public class VerdictEntity {

    private String fileVerdict;
    private String ESAAVVerdict;
    private String ESAAMPVerdict;

    public static VerdictEntity toEntity(@NonNull Verdict verdict) {
        var entity = new VerdictEntity();
        entity.setFileVerdict(entity.fileVerdict);
        entity.setESAAVVerdict(verdict.ESAAVVerdict());
        entity.setESAAMPVerdict(verdict.ESAAMPVerdict());

        return entity;
    }

    public Verdict toDomain() {
        return new Verdict(
                toDefaultVerdict(fileVerdict),
                toDefaultVerdict(ESAAVVerdict),
                toDefaultVerdict(ESAAMPVerdict)
        );
    }

    private static String toDefaultVerdict(String verdict) {
        return Optional.ofNullable(verdict)
                .filter(StringUtils::isNotBlank)
                .orElse("NOT_DEFINED");
    }
}
