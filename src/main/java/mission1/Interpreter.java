package mission1;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;

public class Interpreter implements InterpreterInterface {

    private Stack<String> memory = new MyStack<>(); // pile stockant les nombres à traiter
    private Map<String, Double> def = new HashMap(); //Map stockant les définitions => def.get(Key) retourne la valeur associé à la clé
    private final static String [] keyword = {"pstack", "add", "sub", "mul", "div", "dup","exch", "eq", "ne", "def", "pop"};
    private String display="";

    @Override
    public String interpret(String instructions) {
        Class<? extends Interpreter> c = this.getClass(); //récupère la classe actuelle
        Method m;
        instructions = instructions.toLowerCase(); //met toutes les instructions en minuscule
        instructions = instructions.replace("\n", " "); // retire les retours à la ligne des instructions
        String[] str = instructions.split(" ");
        Method[] method = c.getMethods();
		System.out.println("il y a "+method.length+" méthodes dans cette classe");
		for(int i=0;i<method.length;i++)
		{
				System.out.println(method[i]);
		}
        int i;
        for (i = 0; i < str.length; i++)
        {
            
        	/*
        	 * Si c'est un mot clé, on appele la fonction associée
        	 * Sinon on met la string en mémoire, c'est alors un nombre, un boolean ou une definition
        	 */
            if (Arrays.asList(keyword).contains(str[i]))
            {
                try
                {
                	m=c.getMethod(str[i]); // recupere la methode nomee str[i]
                	m.invoke(this); //execute la methode recuperer ci-dessus
				}
                catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
                {
					e.printStackTrace();
				}
            }
            else
            {
            	memory.push(str[i]);
            }
        }
        return this.display;
    }

    /*
     * Converti la string en nombre et si ce n'est pas un nombre, regarde si elle existe dans les definitions
     * @param arg, string a analyser
     * @return les arguments s
     * @throw lance une exception si arg n'est pas un double et ne se trouve pas dans la liste de defintion.
     */
    private Double toDouble(String arg)
    {
    	Double argD = 0.0;
        try
        {
        	Double.parseDouble(arg);
        }
        catch (NumberFormatException e)
        {
        	if(def.containsKey(arg))
        	{
        		argD=def.get(arg);
        	}
        	else
        	{
        		throw new IllegalArgumentException(arg+" is not an number and doesn't exist in def var");
        	}
        }
        return argD;
    }

    private boolean isDouble(String string) {
        try {
            Double.parseDouble(string);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    //!!! pstack doit écrire dans la string display et pas a l'écran car iterpreteur retourne le contenu de 
    //display !!!
    public void pstack() {
        Stack<String> mystackbis = memory;
        if (mystackbis == null) {
            System.out.print("");
        }
        else {
            if (def.containsKey(mystackbis.peek())) {
                System.out.print(def.get(mystackbis.pop()));
            }
            else {
                System.out.print(mystackbis.pop());
            }
            while (!mystackbis.empty()) {
                if (def.containsKey(mystackbis.peek())) {
                    System.out.print(" " + def.get(mystackbis.pop()));
                }
                else {
                    System.out.print(" " + mystackbis.pop());
                }
            }
        }
    }

    public double add() {
        return 0;
    }

    public double sub() {
        return 0;
    }

    public double mul() {
        return 0;
    }

    public double div() {
        return 0;
    }

    public void dup() {

    }

    public void exch() {

	}

	private boolean eq() { //TODO!!! utiliser la définition de la fonction pas la changer
		String first = memory.pop();
		String second = memory.pop();
		if(def.containsKey(first) && def.containsKey(second)) {
            if(def.get(first) == def.get(second)) {
                memory.push("true");
                return true;
            }
            else {
                memory.push("false");
                return false;
            }
        }

        else if(def.containsKey(first) && !def.containsKey(second)) {
            if(def.get(first) == toDouble(second)) {
                memory.push("true");
                return true;
            }
            else {
                memory.push("false");
                return false;
            }
        }

        else if(!def.containsKey(first) && def.containsKey(second)) {
            if(toDouble(first) == def.get(second)) {
                memory.push("true");
                return true;
            }
            else {
                memory.push("false");
                return false;
            }
        }

        else {
            if(toDouble(first) == toDouble(second)) {
                memory.push("true");
                return true;
            }
            else {
                memory.push("false");
                return false;
            }
        }
	}

    public boolean ne() {
        boolean bool = eq();
        memory.pop();
        if(bool) {
            memory.push("false");
            return false;
        }
        else{
            memory.push("true");
            return true;
        }
    }

    public boolean def() {

        //si la clé est un mot clé utilisé par le programme, retourne une erreur
    	Double value = toDouble(memory.pop());
    	String key = memory.pop();
        if(Arrays.asList(keyword).contains(key))
        {
            throw new IllegalArgumentException("the key is a keywoord");
        }
        else
        {
            key = key.substring(1);// retire le caractère "\" de la string
            def.put(key,value);
            return true;
            
        }
    }

    public void pop() {
        memory.pop();
    }
}
