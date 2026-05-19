import { describe, it, expect, vi, afterEach } from 'vitest'
import { render, screen, fireEvent, cleanup } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import Groups from './Groups'

const mockGroups = [
  { id: 1, name: 'Group A' },
  { id: 2, name: 'Group B' },
]

const mockMemberships = [
  { group: { id: 1, name: 'Group A' }, role: 'OWNER' },
]

vi.mock('../api/axios', () => ({
  default: {
    get: vi.fn(),
    post: vi.fn(),
    delete: vi.fn(),
  },
}))

const api = await import('../api/axios')

afterEach(() => {
  cleanup()
  vi.clearAllMocks()
})

describe('Groups page', () => {
  it('renders the group list and shows all groups', async () => {
    api.default.get.mockImplementation((url) => {
      if (url === '/api/groups') {
        return Promise.resolve({ data: mockGroups })
      }
      if (url === '/api/groups/my-memberships') {
        return Promise.resolve({ data: mockMemberships })
      }
      return Promise.resolve({ data: [] })
    })

    render(
      <MemoryRouter>
        <Groups />
      </MemoryRouter>,
    )

    // Group A appears in both "All Groups" and "My Groups" (owner), so expect two occurrences
    expect(await screen.findAllByText('Group A')).toHaveLength(2)
    expect(await screen.findByText('Group B')).toBeInTheDocument()
    // At least one Open button should exist (open in lists), Join should appear once for Group B
    expect(screen.getAllByText('Open').length).toBeGreaterThanOrEqual(1)
    expect(screen.getAllByText('Join')).toHaveLength(1)
  })
})
