package streaming.soapservice.models;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2020-04-03T20:52:24")
@StaticMetamodel(Movies.class)
public class Movies_ { 

    public static volatile SingularAttribute<Movies, byte[]> thumb;
    public static volatile SingularAttribute<Movies, String> director;
    public static volatile SingularAttribute<Movies, Long> rating;
    public static volatile SingularAttribute<Movies, String> description;
    public static volatile SingularAttribute<Movies, String> producer;
    public static volatile SingularAttribute<Movies, BigDecimal> id;
    public static volatile SingularAttribute<Movies, String> title;
    public static volatile SingularAttribute<Movies, String> category;

}