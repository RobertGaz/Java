package structures;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class TemporalNegativeDataPropertyTriple extends TemporalPropertyTriple {
   private OWLDataProperty dp;
   private OWLLiteral literal;

   public TemporalNegativeDataPropertyTriple(OWLNamedIndividual var1, OWLLiteral var2, OWLNamedIndividual var3, OWLObjectProperty var4, OWLDataProperty var5) {
      this.domain = var1;
      this.literal = var2;
      this.interval = var3;
      this.op = var4;
      this.dp = var5;
   }

   public TemporalNegativeDataPropertyTriple() {
      this.dp = null;
      this.literal = null;
   }

   public void setObject(OWLLiteral var1) {
      this.literal = var1;
   }

   public void setDataProperty(OWLDataProperty var1) {
      this.dp = var1;
   }

   public OWLLiteral getObject() {
      return this.literal;
   }

   public OWLDataProperty getDataProperty() {
      return this.dp;
   }

   public boolean isTemporal() {
      return this.interval != null && this.op != null;
   }

   public boolean isObjectPropertyTriple() {
      return false;
   }

   public boolean isNegative() {
      return true;
   }
}
