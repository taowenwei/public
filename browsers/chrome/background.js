chrome.runtime.onInstalled.addListener(() => {
  console.log("Scrubber extension installed");
});

const scrubAdsUrls = () => {
  console.log("retrieving all image urls");
  let imgs = document.getElementsByTagName("img");
  for (let img of imgs) {
    if (img.src.indexOf("https://img.avdb.me/ad") >= 0) {
      console.log(`scrubbed ${img.src}`);
      img.src = "";
    }
    if (img.src.indexOf("https://ad.avdb.me/") >= 0) {
      console.log(`scrubbed ${img.src}`);
      img.src = "";
    }
    if (img.src.indexOf("https://img.molimao.top/wy/upload/") >= 0) {
      console.log(`scrubbed ${img.src}`);
      img.src = "";
    }
  }
};

chrome.action.onClicked.addListener((tab) => {
  console.log(`Scrubber action clicked for ${tab.id}`);
  chrome.scripting.executeScript({
    target: { tabId: tab.id },
    function: scrubAdsUrls,
  });
});

chrome.webNavigation.onDOMContentLoaded.addListener((details) => {
  console.log(`web page loading completed for ${JSON.stringify(details)}`);
  chrome.scripting.executeScript({
    target: { tabId: details.tabId },
    function: scrubAdsUrls,
  });
});
