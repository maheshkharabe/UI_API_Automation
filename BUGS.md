#Bug1- UI
The car names displayed on UI, few are hyperlinks which navigate to add sites and some are not.

test05_VerifyCarNameDisplayedIsHyperLink
java.lang.AssertionError: Few validations failed for test: checking car names are hyper links
Car Name:VW Polo ,is NOT hyper link expected [true] but found [false],
Car Name:Renault Clio ,is NOT hyper link expected [true] but found [false],
Car Name:Renault Megane ,is NOT hyper link expected [true] but found [false]

#Bug2- UI
Car Prices displayed on UI gets mixed with add links

test03_VerifyCarDetailsDisplayedAreCorrect
java.lang.AssertionError: Few validations failed for test: cars details are correctly displayed as per API response
Car Price is incorrectly displayed expected [€24,000] but found [€24,000QA certification prep]