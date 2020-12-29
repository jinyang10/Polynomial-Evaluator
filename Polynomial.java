package assignment2;

import java.math.BigInteger;
import java.util.Iterator;

public class Polynomial implements DeepClone<Polynomial>
{
 private SLinkedList<Term> polynomial;
 public int size()
 { 
  return polynomial.size();
 }
 private Polynomial(SLinkedList<Term> p)
 {
  polynomial = p;
 }
 
 public Polynomial()
 {
  polynomial = new SLinkedList<Term>();
 }
 
 // Returns a deep copy of the object.
 public Polynomial deepClone()
 { 
  return new Polynomial(polynomial.deepClone());
 }
 
 /* 
  * TODO: Add new term to the polynomial. Also ensure the polynomial is
  * in decreasing order of exponent.
  */
 public void addTerm(Term t)
 {
	 int index = 1;
	 int index_equalterms = 0;
	 
	 if (polynomial.size() == 0) {
		 polynomial.addFirst(t);
	 } else {
		 for (Term term : polynomial) {
			 if (t.getExponent() > term.getExponent()) {
				 polynomial.addFirst(t);
				 break;
				 
			 } else if (t.getExponent() == term.getExponent()) {
				 BigInteger res = t.getCoefficient().add(term.getCoefficient());
				 term.setCoefficient(res);
				//check if adding coefficients =0
				 
				 if (res.compareTo(BigInteger.ZERO) == 0) {
					 polynomial.remove(index_equalterms); 
				 }
				 break;
				 
			 } else if (t.getExponent() < term.getExponent()) {
				 if (polynomial.size() <= 1) {
					 polynomial.addLast(t);
					 break;
					 
				 } else if (index == (polynomial.size() - 1) && polynomial.get(index).getExponent() > t.getExponent()) {
					 polynomial.addLast(t);
					 break;
				 }
				 
				 else if (polynomial.get(index).getExponent() == t.getExponent()) {
					 index_equalterms++;
					 continue;	 
				 } else if (polynomial.get(index).getExponent() < t.getExponent()) {
					 polynomial.add(index, t);
					 break; 
				 }
				 index++;	 
			 } 
			 
			 
				  
			 }
		 }
	 
		 
	 }
	 
	 
	 
  // Hint: Notice that the function SLinkedList.get(index) method is O(n), 
  // so if this method were to call the get(index) 
  // method n times then the method would be O(n^2).
  // Instead, use a Java enhanced for loop to iterate through 
  // the terms of an SLinkedList.
  /*
  for (Term currentTerm: polynomial)
  {
   // The for loop iterates over each term in the polynomial!!
   // Example: System.out.println(currentTerm.getExponent()) should print the exponents of each term in the polynomial when it is not empty.  
  }
  */
 
 public Term getTerm(int index)
 {
  return polynomial.get(index);
 }
 
 //TODO: Add two polynomial without modifying either
 public static Polynomial add(Polynomial p1, Polynomial p2)
 {
  /**** ADD CODE HERE ****/
	 Polynomial pnew = p1.deepClone();
	 
	 for (Term term : p2.polynomial) {
		 pnew.addTerm(term);
	 }
	 return pnew; 
 }
 
 //TODO: multiply this polynomial by a given term.
 public void multiplyTerm(Term t)
 {
	 
	 for (Term term : polynomial) {
		 if (polynomial.size() == 0) {
			 break;
		 } else {
			 term.setExponent(t.getExponent() + term.getExponent());
			 
			 BigInteger res = t.getCoefficient().multiply(term.getCoefficient());
			 term.setCoefficient(res); 
		 }
		 
	 }
 }
 
 //TODO: multiply two polynomials
 public static Polynomial multiply(Polynomial p1, Polynomial p2)
 {
	 Polynomial p1_clone = p1.deepClone();
	 Polynomial p1_nochange = p1.deepClone();
	 Polynomial res = new Polynomial();
	 
	 if (p1.size() == 0 || p2.size() == 0) {
		 return res;
	 }
	 
	 for (Term p2_term : p2.polynomial) {
		 p1_clone.multiplyTerm(p2_term);
		 res = Polynomial.add(res, p1_clone);
		 p1_clone = p1_nochange;
	 }
	 return res;
	 
 }
 
 // TODO: evaluate this polynomial.
 // Hint:  The time complexity of eval() must be order O(m), 
 // where m is the largest degree of the polynomial. Notice 
 // that the function SLinkedList.get(index) method is O(m), 
 // so if your eval() method were to call the get(index) 
 // method m times then your eval method would be O(m^2).
 // Instead, use a Java enhanced for loop to iterate through 
 // the terms of an SLinkedList.

 public BigInteger eval(BigInteger x)
 {
	 Term first = polynomial.get(0);
	 BigInteger t_coeff = first.getCoefficient();
	 
	 if (polynomial.size() == (first.getExponent() + 1)) {
		 if (polynomial.size() == 1) {
			 return t_coeff;
			 
		 } else {
			 for (Term term : polynomial) {
				 if (term.getExponent() == first.getExponent()) {
					 t_coeff = t_coeff.multiply(x);
					 continue;
				 }
				 if (term.getExponent() == 0) {
					 t_coeff = t_coeff.add(term.getCoefficient());
					 return t_coeff;
				 }
				 t_coeff = t_coeff.add(term.getCoefficient());
				 t_coeff = t_coeff.multiply(x);
			 }	 
		 } 
 
	 } else if (polynomial.size() < (first.getExponent() + 1)) {
		 int count = 1;
	
		 for (Term term : polynomial) {
			 if (term.getExponent() == first.getExponent()) {
				 BigInteger x_exp = x.pow(term.getExponent() - polynomial.get(count).getExponent());
				 t_coeff = t_coeff.multiply(x_exp);
				 count ++;
				 continue;
			 }
			 // add current term's coefficient to 1st result
			 
			 // check if at last term of polynomial
			 if (count == polynomial.size()) {
				 t_coeff = t_coeff.add(term.getCoefficient());
				 
				 if (term.getExponent() != 0) {
					 BigInteger x_exp = x.pow(term.getExponent());
					 t_coeff = t_coeff.multiply(x_exp);
					 break;
				 } else {
					 break;
				 }
			 
			 } else {
				 t_coeff = t_coeff.add(term.getCoefficient());
				 BigInteger x_exp = x.pow(term.getExponent() - polynomial.get(count).getExponent());
				 t_coeff = t_coeff.multiply(x_exp);
				 count++;
			 }
			 
		 }
		 
	 } 
	
	 return t_coeff;
 }
 
 // Checks if this polynomial is a clone of the input polynomial
 public boolean isDeepClone(Polynomial p)
 { 
  if (p == null || polynomial == null || p.polynomial == null || this.size() != p.size())
   return false;

  int index = 0;
  for (Term term0 : polynomial)
  {
   Term term1 = p.getTerm(index);

   // making sure that p is a deep clone of this
   if (term0.getExponent() != term1.getExponent() ||
     term0.getCoefficient().compareTo(term1.getCoefficient()) != 0 || term1 == term0)  
    return false;

   index++;
  }
  return true;
 }
 
 // This method blindly adds a term to the end of LinkedList polynomial. 
 // Avoid using this method in your implementation as it is only used for testing.
 public void addTermLast(Term t)
 { 
  polynomial.addLast(t);
 }
 
 
 @Override
 public String toString()
 { 
  if (polynomial.size() == 0) return "0";
  return polynomial.toString();
 }
}
