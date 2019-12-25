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

//options with closures
Map map = ['a': 1, 'b': 2]
//parameter sequence with commas
map.each { key,value -> map[key] = value * 2}
assert map == ['a':2, 'b':4]


//assing and call a closure reference
Closure doubler = {key, value -> map[key] = value * 2}
map.forEach(doubler)
assert map == ['a': 4, 'b':8]

def doubleMethod(entry) {
	entry.value = entry.value * 2
}

//references and calls a method as a closure
doubler = this.&doubleMethod
map.each(doubler)
assert map == ['a':8, 'b':16]

//typed parameters in a closure are not checked until runtime, you might expect compile time, but not the case

//calling closures
def adder = { x, y -> return x+y }
assert adder(4,3) == 7
assert adder.call(2,6) == 8

//place closure argument last, to allow abbreviated syntax
def benchmark ( int repeat, Closure worker) {
	def start = System.nanoTime()
	
	//note that the times loop syntax passes "it" as the current counter
	//it takes a closure as an argument 
	//unlike the usual style you have to "call" the closure
	repeat.times { worker(it) }
	def stop = System.nanoTime()
	return stop - start
}

def slow = benchmark(10000) { (int) it / 2 }
def fast = benchmark(10000) { it.intdiv(2) }
assert fast * 2 < slow

println "Slow:$slow Fast:$fast"

//when calling closure, must pass correct number of arguments
//can use defaults
def defadder = { x, y=5 -> return x+y}
assert defadder(4,3) == 7
assert defadder.call(7) == 12


//groovy.lang.Closure is an ordinary class
//closure methods, can get number of parameters and types
def numParams(Closure closure) {
	closure.getMaximumNumberOfParameters()
}

assert numParams { one -> } == 1
assert numParams { one, two -> } == 2

def paramTypes (Closure closure) {
	closure.getParameterTypes()
}

assert paramTypes { String s ->  } == [String]
assert paramTypes { Number n, Date d -> } == [Number, Date]

//currying - really is partial application
//curry method returns a clone of the closure with fixed parameter/s
def mult = { x, y -> return x * y }
def twoTimes = mult.curry(2)
assert twoTimes(5) == 10

//curry binds leftmost parameter, also is ncurry, and rcurry or lcurry
//instead of currying you can just do this:
def twoTimesNonCurry = { y -> mult 2, y} //the application of mult looks a bit odd here, a closure that returns a closure
assert twoTimesNonCurry(5) == 10

//the real power is when the closure's parameters are themselves closures
//log example, use closures for customized version of activity whilst controlling order of execution
def configurator = { format, filter, line ->
	filter(line) ? format(line) : null
}

def appender = { config, append, line ->
	def out = config(line)
	if (out) append(out)
}

//closure that takes a string
def dateFormatter = {line -> "${new Date()}: $line" }

//closure that takes a string
def debugFilter = {line -> line.contains('debug')}

//closure that takes a string
def consoleAppender = {line -> println line}

//returns a function object (closure) with 2 of 3 parameters set
def myConf = configurator.curry(dateFormatter, debugFilter)

//returns a closure with 2 of 3 parameters set
//first is a closure expecting a 3rd parameter to be set
def myLog = appender.curry(myConf, consoleAppender)

myLog('here is some debug message')
myLog('this will not be printed')


//some links for functional with groovy
//â€œPractically Groovy: Functional programming with curried closures,â€� IBM developerWorks, technical topics,
//www.ibm.com/developerworks/library/j-pg08235/.7
 //â€œFunctional thinking: Functional features in Groovy, Part 1; Treasures lurking in Groovy, IBM developer-
//Works, Technical topics, www.ibm.com/developerworks/java/library/j-ft7/.



//closure composition
def fourTimes = twoTimes >> twoTimes
def eightTimes = twoTimes << fourTimes
assert eightTimes(1) == twoTimes(fourTimes(1))

//another composition example
def square = { it * it }
def plusOne = { it + 1 }
def half = { it / 2 }

def c = half >> plusOne >> square
println c(10) // (10 / 2 + 1) ** 2 = 36
//half(10) = 2
//plusOne(2) = 3
//square(3) = 36

//reversed composition
def d = half << plusOne << square
println d(10) // (10 ** 2 + 1) / 2
//square -> plusOne -> half

//memoization
//caching results of computation
def fib
fib = { it < 2 ? 1 : fib(it - 1) + fib(it -2)}
fib = fib.memoize()
assert fib(40) == 165_580_141
def fiba = fib.memoizeAtLeast(1)
def fibb = fib.memoizeAtMost(2)
def fibc = fib.memoizeBetween(5, 10)
println fiba(20)
println fibb(25)
println fibc(30)

//trampoline for tail call optimization for closures
//@TailRecursive for methods
def last
last = { it.size() == 1 ? it.head() : last.trampoline(it.tail())}
last = last.trampoline()
assert last(0..10_000) == 10_000

//classification via isCase method
//closure that returns a boolean and takes single arg
//can be used in grep, switch
def odd = { it % 2 == 1 }
assert [1,2,3].grep(odd) == [1,3]
switch (10) {
	case odd: assert false
}
if (2 in odd) assert false


//closure scopes
def x = 0
10.times { x++ }
assert x == 10
//note you can write to a variable in scope from within a closure
//java lambda's dont' allow this
























