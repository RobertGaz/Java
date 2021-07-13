package structures;

import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;

public class ObjectHasValueStructure extends ConstraintStructure {
   private OWLNamedIndividual range;

   public ObjectHasValueStructure(OWLClass var1, OWLObjectProperty var2, OWLNamedIndividual var3, OWLClassExpression var4, ConstraintAs var5) {
      this.domain = var1;
      this.prop = var2;
      this.range = var3;
      this.constraint = var4;
      this.constrAs = var5;
   }

   public ObjectHasValueStructure() {
   }

   public void setRange(OWLNamedIndividual var1) {
      this.range = var1;
   }

   public void setProperty(OWLObjectProperty var1) {
      this.prop = var1;
   }

   public OWLNamedIndividual getRange() {
      return this.range;
   }

   public OWLObjectProperty getProperty() {
      return (OWLObjectProperty)this.prop;
   }

   public ConstraintType getConstraintType() {
      return ConstraintType.OBJECT_HAS_VALUE;
   }
}
