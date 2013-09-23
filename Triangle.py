def check(a,b,c):
    if (a < 0) | (b < 0) | (c < 0):
        print 'Error'
    else:
        print 'OK'
x = float(raw_input("Please enter an integer: "))
y = float(raw_input("Please enter an integer: "))
z = float(raw_input("Please enter an integer: "))
e = pow(10,-9)
while (x < 0)|(y<0)|(z<0):
    print 'Please enter again'
    x = float(raw_input("Please enter an integer: "))
    y = float(raw_input("Please enter an integer: "))
    z = float(raw_input("Please enter an integer: "))
    
    check(x,y,z)        
def checkTriangle(a,b,c):
    if (a>=b+c)|(b>=c+a)|(c>=b+a):
        print 'it is not a Triangle'
    else:
        if a==b==c:
            print 'It is a equilateral triangle'
            return
        if (a==b)|(b==c)|(c==a):
            if (a*a-b*b-c*c<=e)|(b*b-c*c-a*a<e)|(c*c-b*b-a*a<e):
                print 'It is a  angled isosceles triangle'
            else:
                print 'It is a isosceles triangle'
        if (a*a-b*b-c*c<e)|(b*b-c*c-a*a<e)|(c*c-b*b-a*a<e):
            if (a!=b)&(b!=c)&(c!=a):
                print 'It is a  angled triangle'
checkTriangle(x,y,z)
