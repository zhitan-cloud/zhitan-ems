import Cookies from 'js-cookie'

const useAppStore = defineStore(
  'app',
  {
    state: () => ({
      sidebar: {
        opened: sessionStorage.getItem('sidebarStatus') ? !!+sessionStorage.getItem('sidebarStatus') : true,
        withoutAnimation: false,
        hide: sessionStorage.getItem('sidebarHide') ? JSON.parse(sessionStorage.getItem('sidebarHide')) : false
      },
      device: 'desktop',
      size: sessionStorage.getItem('size') || 'default'
    }),
    actions: {
      toggleSideBar(withoutAnimation) {
        if (this.sidebar.hide) {
          return false;
        }
        this.sidebar.opened = !this.sidebar.opened
        this.sidebar.withoutAnimation = withoutAnimation
        if (this.sidebar.opened) {
          sessionStorage.setItem('sidebarStatus', 1)
        } else {
          sessionStorage.setItem('sidebarStatus', 0)
        }
      },
      closeSideBar({ withoutAnimation }) {
        sessionStorage.setItem('sidebarStatus', 0)
        this.sidebar.opened = false
        this.sidebar.withoutAnimation = withoutAnimation
      },
      toggleDevice(device) {
        this.device = device
      },
      setSize(size) {
        this.size = size;
        sessionStorage.setItem('size', size)
      },
      toggleSideBarHide(status) {
        this.sidebar.hide = status
        sessionStorage.setItem('sidebarHide', status)
      },
      openMenu() {
        this.sidebar.hide = false
        this.sidebar.opened = true
        sessionStorage.setItem('sidebarHide', 'false')
        sessionStorage.setItem('sidebarStatus', 1)
      },
      showCollapsedSidebar() {
        this.sidebar.hide = false
        this.sidebar.opened = false
        this.sidebar.withoutAnimation = false
        sessionStorage.setItem('sidebarHide', 'false')
        sessionStorage.setItem('sidebarStatus', 0)
      }
    }
  })

export default useAppStore
