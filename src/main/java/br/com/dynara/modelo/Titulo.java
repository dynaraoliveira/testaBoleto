package br.com.dynara.modelo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Titulo {
	
	private String linha;
	private String codBarras;
	private Double valor;
	private String vencimento;
	private boolean banco;
	private boolean valido;
	
	public Titulo(String linha){
		setLinha(linha);
		if (validaLinha()) {
			setBanco();
			setValor();
			setVencimento();
			setCodBarras();
			setValido();
		}
		
	}
	
	public String getLinha() {
		return this.linha;
	}
	
	public String getCodBarras() {
		return this.codBarras;
	}
	
	public Double getValor() {
		return this.valor;
	}
	
	public String getVencimento() {
		return this.vencimento;
	}
	
	public boolean getBanco() {
		return this.banco;
	}
	
	public boolean getValido() {
		return this.valido;
	}
	
	private void setLinha(String linha) {
		char[] c = linha.toCharArray();
	    
	    String auxLinha = "";
	    
	    for (int i = 0; i < c.length; i++) {
	        if (Character.isDigit(c[i])) {
	        	auxLinha = auxLinha.concat(Character.toString(c[i]));
	        }
	    }
		
		this.linha = auxLinha;
	}
	
	private void setValor() {
		String valor = "";
		
		if (!this.banco) {
			valor = this.linha.substring(5,11) + this.linha.substring(12,14) + '.' + this.linha.substring(14,16);
		} else {
			valor = this.linha.substring(38,45) + '.' + this.linha.substring(45);	
		}
		
		this.valor = Double.parseDouble(valor);
	}
	
	private void setCodBarras(){
		String codBar = "";
		if (!this.banco) {
			codBar = codBar.concat(this.linha.substring(0,11));
			codBar = codBar.concat(this.linha.substring(12,23));
			codBar = codBar.concat(this.linha.substring(24,35));
			codBar = codBar.concat(this.linha.substring(36,47));
		} else { 
			codBar = codBar.concat(this.linha.substring(0,4));
			codBar = codBar.concat(this.linha.substring(32));
			codBar = codBar.concat(this.linha.substring(4,9));
			codBar = codBar.concat(this.linha.substring(10,20));
			codBar = codBar.concat(this.linha.substring(21,31));
		}
		this.codBarras = codBar;
	}
	
	private void setVencimento() {
		SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
		Calendar c = null;
		
		if (!this.banco) {
			
			int ano = Integer.parseInt(this.linha.substring(20,23)+this.linha.substring(24,25));
			int mes = Integer.parseInt(this.linha.substring(25,27));
			int dia = Integer.parseInt(this.linha.substring(27,29));

			c = new GregorianCalendar(ano, mes-1, dia);
			
		} else {
		
			int fator = Integer.parseInt(this.linha.substring(33,37));
			
			if (fator != 0) {
				c = new GregorianCalendar(1997, 9, 7);
				c.add(Calendar.DAY_OF_MONTH, fator);
			}
			
		}
		
		if (c!=null) {
			this.vencimento = sd.format(c.getTime());
		} else {
			this.vencimento = "Aceita pagamento em qualquer data";
		}
	}
	
	private void setBanco() {
		if (!this.linha.startsWith("8")) {
			this.banco = true;
		} else {
			this.banco = false;
		}
	}
	
	private void setValido(){
		
		if (!this.banco) {
			
			int digito = Integer.parseInt(this.codBarras.substring(3,4));
			
			int valorEfetivo = Integer.parseInt(this.codBarras.substring(2,3));
			
			if (valorEfetivo==7 || valorEfetivo==6) {
				if (modulo10(this.codBarras)!=digito) {
					this.valido = false;
				} else {
					this.valido = true;
				}
			} else {
				if (modulo11(this.codBarras)!=digito) {
					this.valido = false;
				} else {
					this.valido = true;
				}
			}
			
		} else {
			int digito = Integer.parseInt(this.codBarras.substring(4,5));
			
			if (modulo11(this.codBarras)!=digito) {
				this.valido = false;
			} else {
				this.valido = true;
			}
		}	
		
			
	}
	
	private boolean validaLinha() {
		if (this.linha.length() == 47 || this.linha.length() == 48) {
			return true;
		}
		return false;
	}
	
	public int modulo11(String valor) {
		
		int charDigitoDesprezar = 4;
		if (!this.banco) {
			charDigitoDesprezar = 3;
		}
		
		int soma = 0;
		int peso = 2;
		int base = 9;
		
		int contador = valor.length() - 1;
		
		int x = 0;
		
		while (contador >= 0) {
			
			if (contador!=charDigitoDesprezar) {
				x = Integer.parseInt(Character.toString(valor.charAt(contador)));
				
				soma += (x*peso);
				
				if (peso < base) {
					peso +=1;
				} else {
					peso = 2;
				}
			}
			contador-=1;
		}
		
		int digito = 11 - (soma % 11);
		
		if (digito == 0 || digito == 10 || digito == 11) digito = 1;
		
		return digito;
	}

	public int modulo10(String valor) {
		
		int charDigitoDesprezar = 4;
		if (!this.banco) {
			charDigitoDesprezar = 3;
		}
		
		int soma = 0;
		int peso = 2;
		int contador = valor.length() - 1;
		int x = 0;
		
		while (contador >= 0) {
		
			if (contador!=charDigitoDesprezar) {
				
				x = Integer.parseInt(Character.toString(valor.charAt(contador)));
				
				int multiplicacao = (x*peso);
				
				if (multiplicacao > 9) {
					multiplicacao -= 9;
				}
				
				soma += multiplicacao;
				
				if (peso == 2) {
					peso = 1;
				} else {
					peso = 2;
				}
			}
			contador -= 1;
		}

		int digito = 10 - (soma % 10);

		if (digito == 10) digito = 0;

		return digito;
		
	}

}

