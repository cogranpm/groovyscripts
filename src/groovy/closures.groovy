package groovy


//java 8 iteration using streams (got to use a groovy closure instead of a lambda)
def list = [1,2,3]
list.stream().forEach { println it }
//the java syntax for lambda is
//.forEach ( (arg) -> { do something })

//closures for handling resources
new File('myfile.txt').withWriter('utf-8') { writer ->
	writer.writeLine("hello from groovy")
}
new File('myfile.txt').eachLine { println it }
//file will be correctly closed


//java frequently uses anonymous inner classes for resource handling

//if closure is last parameter of method, no need for ()
def log = ''
list.each({ log += it})
//or
list.each { log += it}

//assign closure to variable
def printer = { line -> println line}

//return value (can't do this with java 8 lambda
def Closure getPrinter() {
	return {line -> println line }
}

//reference existing method as a closure .& operator

class SizeFilter {
	Integer limit
	
	boolean sizeUpTo(String value) {
		return value.size() <= limit
	}
}

SizeFilter filter6 = new SizeFilter(limit: 6)
SizeFilter filter5 = new SizeFilter(limit: 5)

Closure sizeUpTo6 = filter6.&sizeUpTo  //.& with instance on left and method reference on right

def words = ['long string', 'medium', 'short', 'tiny']
assert 'medium' == words.find(sizeUpTo6)
assert 'short' == words.find(filter5.&sizeUpTo)

//only works with instance methods, so no static methods
//do support multimethods, ie method overloading with differing parameters, at runtime correct method will be called depending on argument
class MultiMethodSample{
	int mysteryMethod (String value) {
		return value.length()
	}
	
	int mysteryMethod(List list) {
		return list.size()
	}
	
	int mysteryMethod(int x, int y) {
		return x+y
	}
}

MultiMethodSample instance = new MultiMethodSample()
Closure multi = instance.&mysteryMethod

assert 10 == multi ('string arg')
assert 3 == multi (['list', 'of', 'values'])
assert 14 == multi(6,8)



























