import html2pdf from 'html2pdf.js';

export default {
  install(Vue, options) {
    Vue.prototype.htmlToPdf = function (refs, fileName = '导出pdf') {
      let element = refs
      let opt = {
        margin: 40,
        filename: `${fileName}.pdf`,
        image: {
          type: 'jpeg',
          quality: 1
        },
        html2canvas: {
          scale: 1,
          allowTaint: true,
          useCORS: true
        },
        pagebreak: {
          mode: 'avoid-all',
          after: '.avoidThisRow'
        }, // 智能分页，防止被截断
        enableLinks: true, // 支持文本中放链接，可点击跳转
        jsPDF: {
          unit: 'pt',
          format: 'a2',
        }
      }
      html2pdf(element, opt)
    };
  }
};
