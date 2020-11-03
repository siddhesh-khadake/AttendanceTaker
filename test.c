#include <stdio.h>
#include <string.h>
int main()
{
    //fsubstr()-to check if the given string contains a substring
    printf("\n 1.to check if the given string contains a substring\n");
    char str[100], substr[10];
    int l, i, j;
    printf("Enter string:");
    scanf("%s", str);
    printf("enter substring from string:");
    scanf("%s", substr);
    //finding length of substring
    for (l = 0; substr[l] != '\0'; l++);
    for (i = 0, j = 0; str[i] != '\0' && substr[j] != '\0'; i++)
    {
        if (str[i] == substr[j])
        {
            j++;
        }
        else
        {
            j = 0;
        }
    }
    if (j == l)
    {
        printf("substring found at position %d", i - j + 1);
    }
    else
    {
        printf("substring not found");
    }
    //2.fconcat()- to join two strings
    printf("\n\n 2. to join two strings\n");
    char str1[100];
    char str2[100];
    printf("enter the first string to join:");
    scanf("%s", str1);
    printf("enter the second string to join:");
    scanf("%s", str2);
    strcat(str1, str2);
    puts(str1);
    //3.fcompare() - to compare two strings and display "strings are same" 
    printf("\n 3. to compare two strings\n");
    char str3[100];
    char str4[100];
    int value;
    printf("enter the first string to compare:");
    scanf("%s", str3);
    printf("enter the second string to compare:");
    scanf("%s",str4);
    value=strcmp(str3,str4);
    if(value==0)
    {
        printf("strings are same");
    }
    else
    {
        printf("strings are not same");
    }
    //4.fLen()-to find the length of the string withoud using a built-in function
    printf("\n\n 4.to find the length of the string withoud using a built-in function \n");
    char s[100];
    printf("enter the string:");
    scanf("%s",s);
    int a;
    for(a=0; s[a] !='\0'; ++a);
    printf("length of the string is :%d\n",a);
    return 0;
}