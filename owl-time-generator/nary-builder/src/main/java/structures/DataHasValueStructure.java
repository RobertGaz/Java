package structures;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;

public class DataHasValueStructure extends ConstraintStructure {
   private String range;

   public DataHasValueStructure(OWLClass var1, OWLDataProperty var2, String var3, OWLClassExpression var4, ConstraintAs var5) {
      this.domain = var1;
      this.prop = var2;
      this.range = var3;
      this.constraint = var4;
      this.constrAs = var5;
   }

   public DataHasValueStructure() {
   }

   public void setRange(String var1) {
      this.range = var1;
   }

   public void setProperty(OWLDataProperty var1) {
      this.prop = var1;
   }

   public String getRange() {
      return this.range;
   }

   public OWLDataProperty getProperty() {
      return (OWLDataProperty)this.prop;
   }

   public ConstraintType getConstraintType() {
      return ConstraintType.DATA_HAS_VALUE;
   }
}
