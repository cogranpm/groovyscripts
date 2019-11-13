package groovy
//groovy uses a withCloseable construct instead of try-with-resources


//use a final before the exception declaration when throwing

//catch( final Exception e) {
//	throw e
	//}	

//multicatch

def myMethod() {
	throw new IllegalArgumentException()
}

def log = []
try {
	myMethod()
} catch (Exception e) {
	log << e.toString()
} finally {
	log << 'finally'
}

println log

def throwFinal() {
	try {
		FileInputStream fin = new FileInputStream("x.txt")
	} catch (final FileNotFoundException e) {
		throw e
	}
}

try {
	throwFinal()
} catch (Exception e) {
	println e
}

def multiCatch() {
	try {
		if (Math.random() < 0.5) 1 / 0
		else null.hashCode()
	} catch (ArithmeticException | NullPointerException ex) {
		println ex.class.name
	}
}

multiCatch()




