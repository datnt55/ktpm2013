import math
def detect_triangle(a,b,c):
    if (type(a) in [int,long,float]) and (type(c) in [int,long,float])and (type(c) in [int,long,float]):
        if ((a>0)and(b>0)and(c>0))and((a<math.pow(2,32)) and (b<math.pow(2,32)) and (c<math.pow(2,32))):
            e = pow(10,-9)
            if (a>=b+c)|(b>=c+a)|(c>=b+a):
                return "Khong phai tam giac"
            else:
                if a==b==c:
                    return "Tam giac deu"
                if (a==b)|(b==c)|(c == a):
                    if (math.fabs(a*a-b*b-c*c)<e)|(math.fabs(b*b-c*c-a*a)<e)|(math.fabs(c*c-b*b-a*a)<e):
                        return "Tam giac vuong can"
                    else:
                        return "Tam giac can"
                if (math.fabs(a*a-b*b-c*c)<e)|(math.fabs(b*b-c*c-a*a)<e)|(math.fabs(c*c-b*b-a*a)<e):
                    if (a!=b)&(b!=c)&(c!=a):
                        return "Tam giac vuong"
                return "Tam giac thuong"
        else:
            return "Ngoai khoang gia tri"
    else:
        return "Kieu khong hop le"
