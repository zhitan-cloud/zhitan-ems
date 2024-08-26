/**
 * 折线图的自适应
 * 当窗口发生变化的时候 会重新的更新视图的位置 达到自适应
 */
export default {
    data() {
        return {
            bodyStyle: {
                height: '',
                overflow: 'auto'
            }
        }
    },
    mounted() {
        this.setCharts();
        window.addEventListener('resize', this.setCharts);
    },
    beforeDestroy() {
        window.removeEventListener('resize', this.setCharts);
    },
    methods: {

    }
};
