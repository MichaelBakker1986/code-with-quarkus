package nl.appmodel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@ToString
@Getter
@AllArgsConstructor
public class DataObjectPro {
    transient long id;
    transient int  n;
    String sha;
    String header;
    String embed;
}
