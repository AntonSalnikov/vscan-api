package com.dataslab.vscan.service.file;

import com.dataslab.vscan.dto.ScanResult;
import com.dataslab.vscan.service.domain.Verdict;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

public class ScanResultResolver {

    private ScanResultResolver() {
    }

    /**
     ESAAVVerdict:

         NOT EVALUATED — не оцінювався,
         NEGATIVE — все чисто,
         REPAIRED - ?
         ENCRYPTED — файл зашифрований/запаролений (ми такі файли можемо відкидати фільтром) ,
         UNSCANNABLE — не змогли просканувати з інших причин,
         POSITIVE — знайден шкідливий код
         NO_VERDICT

     ESAAMPVerdict:

         NOT EVALUATED — не оцінювався
         CLEAN — файл чистий
         FA_PENDING — файл був відправлений в пісочницю для аналіза
         UNKNOWN — немає інформації про файл, можна його пропускати
         SKIPPED — сканування не відбулося
         UNSCANNABLE  — неможливо проаналізувати файл
         LOW_RISK — незаважучі на підозрілість, ризику немає, можна пропускати
         MALICIOUS — шкідливий код, заблокувати
         NO_VERDICT
     **/
    public static ScanResult resolve(Verdict verdict) {

        if(verdict == null) {
            return ScanResult.UNKNOWN;
        }

        var verdicts = List.of(verdict.ESAAMPVerdict(), verdict.ESAAVVerdict());

        if(CollectionUtils.containsAny(verdicts, "POSITIVE", "MALICIOUS")) {
            return ScanResult.INFECTED;
        }

        if(CollectionUtils.containsAny(verdicts, "FA_PENDING","ENCRYPTED", "UNSCANNABLE", "SKIPPED")) {
            return ScanResult.UNKNOWN;
        }

        if(CollectionUtils.containsAny(verdicts, "NEGATIVE", "CLEAN", "UNKNOWN", "LOW_RISK")) {
            return ScanResult.CLEAN;
        }

        return ScanResult.UNKNOWN;
    }
}
