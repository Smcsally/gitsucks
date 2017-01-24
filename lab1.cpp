#include <iostream>
using namespace std;

int main()
{
	double elements;
	double sum = 0;
	double temp;
	cout << "Enter a number of elements: ";
	cin >> elements;
	for (int i = 0; i < elements; i++)
	{
		cout << "Enter value: ";
		cin >> temp;
		sum += temp;
	}
	cout << "Average is " << sum / elements << endl;
}

