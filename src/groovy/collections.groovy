package groovy


//ranges
assert (0..10).contains(10)
assert (0..<10).contains(9)
assert (0..<10).contains(10) == false

//explicit construction
def a = new IntRange(0,10)

//date range


//string range
assert('a'..'c').contains('b')

//for in range loop
def log = ''
for (element in 5..9) {
	log += element
}

println log

//.each is an alternative
log = ''
(9..<5).each { element ->
	log += element
}

//important methods on a range
//each and contains

//can be used in switches
def age = 36
switch (age) {
	case 16..20 : println 'young'; break
	case 21..50 : println 'old'; break
	default: println 'really young or old'
}


def ages = [20, 36, 42, 56]
def midage = 21..50
//can filter with a range
assert ages.grep(midage) == [36, 42]

//ranges very handy in the business world
//any datatype can be used with a range as long as it:
	// implements next and previous
	// implements compareTo

class Weekday implements Comparable {
	static final DAYS = [
		'Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'
	]
	
	private int index = 0
	
	Weekday(String day) {
		index = DAYS.indexOf(day)
	}
	
	Weekday next() {
		return new Weekday(DAYS[(index + 1) % DAYS.size()])
	}
	
	Weekday previous() {
		return new Weekday(DAYS[index - 1])
	}
	
	int compareTo(Object other) {
		return this.index <=> other.index
	}
	
	String toString() {
		return DAYS[index]
	}
	
}

def mon = new Weekday('Mon')
def fri = new Weekday('Fri')

def worklog = ''
for (day in mon..fri) {
	worklog += day.toString() + ' '
}

println worklog


//LISTS
//are instances of java.util.ArrayList
List myList = [1,2,3]
assert myList.size()== 3
assert myList[0] == 1
assert myList instanceof ArrayList

List emptyList = []
assert emptyList.size() == 0
List longList = (0..1000).toList()
assert longList[555] == 555

List explicitList = new ArrayList()
explicitList.addAll(myList)
assert explicitList.size() == 3
explicitList[0] = 10
assert explicitList[0] == 10

//gdk gives toList method to all arrays, collection objects and strings

myList = ['a', 'b', 'c', 'd', 'e', 'f']
println myList[0..2]

































