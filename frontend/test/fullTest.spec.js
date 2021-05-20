const {Builder, By, Key} = require("selenium-webdriver");
async function example(){
  let driver = await new Builder().forBrowser("firefox").build();
  await driver.get("https://google.com");
  await driver.findElement(By.name("q")).sendKeys("Bavo is ros", Key.RETURN)
};
example()