package structures;

import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class TemporalDataPropertyTriple extends TemporalPropertyTriple {
   private OWLDataProperty dp;
   private OWLLiteral literal;

   public TemporalDataPropertyTriple(OWLNamedIndividual var1, OWLNamedIndividual var2, OWLLiteral var3, OWLNamedIndividual var4, OWLObjectProperty var5, OWLDataProperty var6) {
      this.domain = var1;
      this.eventInd = var2;
      this.literal = var3;
      this.interval = var4;
      this.op = var5;
      this.dp = var6;
   }

   public TemporalDataPropertyTriple() {
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
      return this.eventInd != null && this.interval != null && this.op != null;
   }

   public boolean isObjectPropertyTriple() {
      return false;
   }

   public boolean isNegative() {
      return false;
   }
}
