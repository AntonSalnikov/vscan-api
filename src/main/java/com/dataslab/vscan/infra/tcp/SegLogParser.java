package com.dataslab.vscan.infra.tcp;

import com.dataslab.vscan.service.domain.FileVerificationResult;
import com.dataslab.vscan.service.domain.Verdict;

import java.util.UUID;
import java.util.regex.Pattern;

public class SegLogParser {
    private SegLogParser() {
    }

    //{syslog_DECODE_ERRORS=true, syslog_ERRORS=Expected ' ' @5, syslog_UNDECODED=<22>Jun 17 18:21:38 mriya.organic-invest-llc.com mail_cef_tcp: CEF:0|Cisco|C100V Secure Email Gateway Virtual|15.5.1-055|ESA_CONSOLIDATED_LOG_EVENT|Consolidated Log Event|5|deviceExternalId=564D15CFD52D6783CD9A-FCF6ABEB9F39 ESAMID=41 ESAICID=44 ESAAMPVerdict=UNKNOWN ESAAVVerdict=NEGATIVE ESACFVerdict=MATCH ESAAttachmentDetails={'92a4b53c-faba-4147-9aca-dbccd9c65560': {'IA': {'Score': 0}, 'AMP': {'Verdict': 'FILE UNKNOWN', 'fileHash': 'c4e536294f58642c952b2fa5b3af6b703212d76e8a29643c781a99160df3e514'}, 'BodyScanner': {}}} msg='Scanning file. File id: 92a4b53c-faba-4147-9aca-dbccd9c65560'}

    private static final String NO_VERDICT = "NO_VERDICT";
    private static final Pattern FILE_ID_PATTERN = Pattern.compile("ESAAttachmentDetails=\\{'([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})");
    private static final Pattern SCANNING_FILE_ID_PATTERN = Pattern.compile("File id: ([0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12})");

    private static final Pattern VERDICT_PATTERN = Pattern.compile("'Verdict': '(.*)',");
    private static final Pattern FILE_HASH_PATTERN = Pattern.compile("'fileHash': '(.*)'},");


    /**
       ESAAVVerdict:

       NOT EVALUATED — не оцінювався,
       NEGATIVE — все чисто,
       REPAIRED - ?
       ENCRYPTED — файл зашифрований/запаролений (ми такі файли можемо відкидати фільтром) ,
       UNSCANNABLE — не змогли просканувати з інших причин,
       POSITIVE — знайден шкідливий код
    **/
    private static final Pattern ESAAVVerdict_VERDICT_PATTERN = Pattern.compile("ESAAVVerdict=(NOT EVALUATED|NEGATIVE|REPAIRED|ENCRYPTED|UNSCANNABLE|POSITIVE)");


    /**
      ESAAMPVerdict:

      NOT EVALUATED — не оцінювався
      CLEAN — файл чистий
      FA_PENDING — файл був відправлений в пісочницю для аналіза
      UNKNOWN — немає інформації про файл, можна його пропускати
      SKIPPED — сканування не відбулося
      UNSCANNABLE  — неможливо проаналізувати файл
      LOW_RISK — незаважучі на підозрілість, ризику немає, можна пропускати
      MALICIOUS — шкідливий код, заблокувати
     **/

    private static final Pattern ESAAMP_VERDICT_PATTERN = Pattern.compile("ESAAMPVerdict=(NOT EVALUATED|CLEAN|FA_PENDING|UNKNOWN|SKIPPED|UNSCANNABLE|LOW_RISK|MALICIOUS)");

    public static FileVerificationResult parse(String payload) {

        var messageId = UUID.fromString(resolveFileId(payload));

        var fileHash = resolvePattern(payload, FILE_HASH_PATTERN, "UNDEFINED");

        var verdict = new Verdict(
               resolvePattern(payload, VERDICT_PATTERN, NO_VERDICT),
               resolvePattern(payload, ESAAVVerdict_VERDICT_PATTERN, NO_VERDICT),
               resolvePattern(payload, ESAAMP_VERDICT_PATTERN, NO_VERDICT)
        );
        return new FileVerificationResult(fileHash, verdict, messageId);
    }

    private static String resolvePattern(String payload, Pattern pattern, String defaultValue) {

        var verdictMatcher = pattern.matcher(payload);
        if(!verdictMatcher.find()) {
            return defaultValue;
        }
        return verdictMatcher.group(1).trim();
    }

    private static String resolveFileId(String payload) {

        var messageIdMatcher = FILE_ID_PATTERN.matcher(payload);

        if(messageIdMatcher.find()) {
            return messageIdMatcher.group(1).trim();
        }

        var scanningMessageIdMatcher = SCANNING_FILE_ID_PATTERN.matcher(payload);
        if(scanningMessageIdMatcher.find()) {
            return scanningMessageIdMatcher.group(1).trim();
        }

        throw new IllegalStateException("No message id found");
    }
}
